package com.example.yp_playlist_maker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.hideKeyboard
import com.example.yp_playlist_maker.app.invisible
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.databinding.ActivitySearchBinding
import com.example.yp_playlist_maker.player.ui.AudioPlayerActivity
import com.example.yp_playlist_maker.search.ui.view_model.SearchViewModel
import com.example.yp_playlist_maker.util.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var textWatcher: TextWatcher
    private var savedSearchText: String = EMPTY_STRING
    private val adapter = TrackAdapter()
    private var updateTrackHistory: Boolean = false
    private var onRestoreError: String = EMPTY_STRING
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { if (binding.etSearch.text.isNotEmpty()) { search() } }
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSearchActivityViews()
        setRecyclerView()

        setSearchActivityObservers()

        textWatcher = setTextWatcher()

        binding.etSearch.addTextChangedListener(textWatcher)
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etSearch.text.isEmpty() && viewModel.getTrackList().isNotEmpty()) {
                enableSearchHistoryVisibility(true)
            } else {
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
            binding.activitySearch.hideKeyboard()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
            enableSearchHistoryVisibility(false)
        }

        adapter.onTrackClick = {
            if (clickDebounce()) {
                viewModel.saveClickedTrack(it)
                val displayAudioPlayer = Intent(this, AudioPlayerActivity::class.java)
                displayAudioPlayer.apply {
                    putExtra(INTENT_PUTTED_TRACK, it)
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

    private fun setRecyclerView() {
        binding.rvTrack.layoutManager = LinearLayoutManager(this)
        binding.rvTrack.adapter = adapter
    }

    private fun setTextWatcher() : TextWatcher {
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
                    if (viewModel.getTrackHistoryList().isNotEmpty()) {
                        viewModel.getTrackHistory()
                    }
                }
            }
        }

        return simpleTextWatcher
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
    private fun handleSearchStatus(searchState: State.SearchState) {
        when (searchState) {
            State.SearchState.LOADING -> {
                binding.progressBar.visible()
            }
            State.SearchState.SUCCESS -> {
                binding.progressBar.gone()
                binding.rvTrack.visible()
            }
            State.SearchState.SEARCH_ERROR -> {
                showError(getString(R.string.nothing_found))
                onRestoreError = getString(R.string.nothing_found)
            }
            State.SearchState.CONNECTION_ERROR -> {
                showError(getString(R.string.connection_error))
                onRestoreError = getString(R.string.connection_error)
            }
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedSearchText =
            savedInstanceState.getString(getString(R.string.saved_text), savedSearchText)
        handler.removeCallbacks(searchRunnable)
        if (binding.etSearch.text.isNotEmpty() && onRestoreError.isEmpty()) {
            binding.rvTrack.visible()
        } else if (binding.etSearch.text.isNotEmpty() && onRestoreError.isNotEmpty()) {
            showError(onRestoreError)
            onRestoreError = EMPTY_STRING
        }
    }

    // Поиск песен
    private fun search() {
        viewModel.search(binding.etSearch.text.toString())
    }

    private fun showError(error: String) {
        binding.progressBar.gone()
        binding.placeholder.visible()
        when (error) {
            getString(R.string.nothing_found) -> {
                binding.placeholderMessage.text = getString(R.string.nothing_found)
                binding.imgSearchError.setImageResource(R.drawable.img_search_error)
                binding.btnReload.gone()
            }
            getString(R.string.connection_error) -> {
                binding.placeholderMessage.text = getString(R.string.connection_error)
                binding.imgSearchError.setImageResource(R.drawable.img_connection_error)
                binding.btnReload.visible()
            }
        }
    }

    private fun enableSearchHistoryVisibility(enabled: Boolean) {
        binding.apply {
            tvSearchHistory.isVisible = enabled
            btnClearHistory.isVisible = enabled
            rvTrack.isVisible = enabled
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
        private const val INTENT_PUTTED_TRACK: String = "PuttedTrack"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val EMPTY_STRING: String = ""
    }
}
