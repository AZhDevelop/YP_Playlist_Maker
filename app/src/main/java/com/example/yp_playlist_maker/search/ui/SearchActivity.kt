package com.example.yp_playlist_maker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.ActivitySearchBinding
import com.example.yp_playlist_maker.player.ui.AudioPlayerActivity
import com.example.yp_playlist_maker.search.ui.view_model.SearchViewModel
import com.example.yp_playlist_maker.search.ui.view_model.SearchViewModelFactory
import com.example.yp_playlist_maker.settings.ui.gone
import com.example.yp_playlist_maker.settings.ui.hideKeyboard
import com.example.yp_playlist_maker.settings.ui.invisible
import com.example.yp_playlist_maker.settings.ui.visible

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private var savedSearchText: String = EMPTY_STRING
    private val adapter = TrackAdapter()
    private var updateTrackHistory: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { if (binding.etSearch.text.isNotEmpty()) { search() } }
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSearchActivityViews()
        viewModel = ViewModelProvider(this, SearchViewModelFactory())[SearchViewModel::class.java]
        setSearchActivityObservers()

        // RecyclerView для списка песен
        binding.rvTrack.layoutManager = LinearLayoutManager(this)
        binding.rvTrack.adapter = adapter

        viewModel.getTrackHistory()

        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            Log.d("onFocus", "${viewModel.getTrackList().isEmpty()}")
            if (hasFocus && binding.etSearch.text.isEmpty() && viewModel.getTrackList().isNotEmpty()) {
                Log.d("onFocus", "Search history: Visible")
                enableSearchHistoryVisibility(true)
            } else {
                Log.d("onFocus", "Search history: Invisible")
                enableSearchHistoryVisibility(false)
            }
        }

        // Логика запуска поиска песен с клавиатуры
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkPlaceholder()
                search()
            }
            false
        }

        // Кнопка перезапуска поиска песен, если отсутствовал интернет
        binding.btnReload.setOnClickListener {
            checkPlaceholder()
            search()
        }

        // Закрываем Activity
        binding.iwBack.setOnClickListener {
            finish()
        }

        // Очищаем строку и список песен при нажатии на кнопку
        binding.iwClear.setOnClickListener {
            viewModel.clearTrackList()
            binding.etSearch.setText(EMPTY_STRING)
            binding.etSearch.hideKeyboard()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
            enableSearchHistoryVisibility(false)
        }

        // Проверяем сохраненное состояние текста и востанавллиевам, если что-то сохранено
        if (savedInstanceState != null) {
            savedSearchText =
                savedInstanceState.getString(getString(R.string.saved_text), savedSearchText)
            binding.etSearch.setText(savedSearchText)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.iwClear.visibility = clearButtonVisibility(s)
                savedSearchText = binding.etSearch.text.toString()
                enableSearchHistoryVisibility(false)
                if (binding.etSearch.text.isEmpty() && viewModel.getTrackHistoryList().isNotEmpty()) {
                    viewModel.updateTrackList()
                    enableSearchHistoryVisibility(true)
                }
                if (binding.etSearch.text.isEmpty()) {
                    checkPlaceholder()
                }
                if (binding.etSearch.text.isNotEmpty()) {
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.etSearch.text.isEmpty()) {
                    viewModel.clearTrackList()
                }
            }
        }
        binding.etSearch.addTextChangedListener(simpleTextWatcher)

        adapter.onTrackClick = {
            if (clickDebounce()) {
                viewModel.saveClickedTrack(it)
                val displayAudioPlayer = Intent(this, AudioPlayerActivity::class.java)
                displayAudioPlayer.apply {
                    putExtra(AudioPlayerActivity.INTENT_PUTTED_TRACK, it)
                }
                startActivity(displayAudioPlayer)
                if (binding.etSearch.text.isEmpty()) {
                    updateTrackHistory = true
                }
            }
        }
    }

    private fun setSearchActivityViews() {
        binding.apply {
            iwClear.invisible()
            placeholder.gone()
            btnReload.gone()
            btnClearHistory.gone()
            tvSearchHistory.gone()
            rvTrack.gone()
        }
    }

    // Инициализация наблюдателей viewModel
    private fun setSearchActivityObservers() {
        viewModel.getSearchStatus().observe(this) { searchStatus ->
            handleSearchStatus(searchStatus)
        }

        viewModel.getTrackListLiveData().observe(this) { trackListLiveData ->
            adapter.data = trackListLiveData
            adapter.notifyDataSetChanged()
        }
    }

    // Наблюдатель состояние запроса поиска
    private fun handleSearchStatus(searchStatus: String) {
        when (searchStatus) {
            LOADING -> {
                binding.progressBar.visible()
            }
            SUCCESS -> {
                binding.progressBar.gone()
                binding.rvTrack.visible()
            }
            SEARCH_ERROR -> { showError(SEARCH_ERROR) }
            CONNECTION_ERROR -> { showError(CONNECTION_ERROR) }
        }
    }

    // Функция контроля состояния видимости кнопки "Очистить строку" в поисковой строке
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    // Сохранение состояние текста
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putString(getString(R.string.saved_text), savedSearchText)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedSearchText =
            savedInstanceState.getString(getString(R.string.saved_text), savedSearchText)

        // binding.rvTrack.visible()

    }

    // Поиск песен
    private fun search() {
        viewModel.search(binding.etSearch.text.toString())
    }

    private fun showError(error: String) {
        binding.progressBar.gone()
        binding.placeholder.visible()
        when (error) {
            SEARCH_ERROR -> {
                binding.placeholderMessage.text = SEARCH_ERROR
                binding.imgSearchError.setImageResource(R.drawable.img_search_error)
                binding.btnReload.gone()
            }
            CONNECTION_ERROR -> {
                binding.placeholderMessage.text = CONNECTION_ERROR
                binding.imgSearchError.setImageResource(R.drawable.img_connection_error)
                binding.btnReload.visible()
            }
        }
    }

    private fun enableSearchHistoryVisibility(enabled: Boolean) {
        binding.apply {
            if (enabled) {
                tvSearchHistory.visible()
                btnClearHistory.visible()
                rvTrack.visible()
            } else {
                tvSearchHistory.gone()
                btnClearHistory.gone()
                rvTrack.gone()
            }
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        checkPlaceholder()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun checkPlaceholder() {
        if (binding.placeholder.isVisible) {
            binding.placeholder.gone()
        }
    }

    override fun onResume() {
        super.onResume()
        if (updateTrackHistory) {
            viewModel.updateTrackList()
            updateTrackHistory = false
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val EMPTY_STRING: String = ""

        private const val LOADING = "Loading"
        private const val SUCCESS = "Success"
        private const val CONNECTION_ERROR =
            "Проблемы со связью\nЗагрузка не удалась\nПроверьте подключение к интернету"
        private const val SEARCH_ERROR = "Ничего не нашлось"
    }
}
