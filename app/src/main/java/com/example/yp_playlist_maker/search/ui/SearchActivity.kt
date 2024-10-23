package com.example.yp_playlist_maker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.ActivityAudioplayerBinding
import com.example.yp_playlist_maker.databinding.ActivitySearchBinding
import com.example.yp_playlist_maker.search.domain.api.TrackInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.settings.ui.gone
import com.example.yp_playlist_maker.settings.ui.invisible
import com.example.yp_playlist_maker.settings.ui.visible
import com.example.yp_playlist_maker.player.ui.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val trackService = Creator.provideTrackInteractor()
    private var savedSearchText: String = EMPTY_STRING
    private var trackList: ArrayList<Track> = arrayListOf()
    private val adapter = TrackAdapter()
    private val trackHistoryList: ArrayList<Track> = arrayListOf()
    private var updateTrackHistory: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { if (binding.etSearch.text.isNotEmpty()) { search() } }
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackHistoryInteractor = Creator.provideSeacrhHistoryInteractor()
        val trackHistory = trackHistoryInteractor.getHistory()

        binding.apply {
            iwClear.invisible()
            placeholder.gone()
            btnReload.gone()
            btnClearHistory.gone()
            tvSearchHistory.gone()
            rvTrack.gone()
        }

        adapter.data = trackList

        // RecyclerView для списка песен
        binding.rvTrack.layoutManager = LinearLayoutManager(this)
        binding.rvTrack.adapter = adapter

        if (trackHistory.isNotEmpty()) {
            trackList.addAll(trackHistory)
            trackHistoryList.addAll(trackHistory)
            adapter.notifyDataSetChanged()
        }

        binding.etSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.etSearch.text.isEmpty() && trackList.isNotEmpty()) {
                enableSearchHistoryVisibility(true)
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
            trackList.clear()
            adapter.notifyDataSetChanged()
            binding.etSearch.setText(EMPTY_STRING)
            it.hideKeyboard()
        }

        binding.btnClearHistory.setOnClickListener {
            trackHistoryInteractor.clearHistory()
            trackList.clear()
            trackHistoryList.clear()
            adapter.notifyDataSetChanged()
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
                if (binding.etSearch.text.isEmpty() && trackHistoryList.isNotEmpty()) {
                    trackList.clear()
                    trackList.addAll(trackHistoryList)
                    adapter.notifyDataSetChanged()
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
                //
            }
        }
        binding.etSearch.addTextChangedListener(simpleTextWatcher)

        adapter.onTrackClick = {
            if (clickDebounce()) {
                trackHistoryInteractor.saveClickedTrack(it, trackHistoryList)
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

    override fun onResume() {
        super.onResume()
        if (updateTrackHistory) {
            trackList.clear()
            trackList.addAll(trackHistoryList)
            adapter.notifyDataSetChanged()
            updateTrackHistory = false
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
    }

    // Прячем клавиатуру
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    // Поиск песен
    private fun search() {

        binding.progressBar.visible()

        trackService.searchTrack(binding.etSearch.text.toString(), object : TrackInteractor.TrackConsumer {
            override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                handler.post {
                    binding.progressBar.gone()
                    trackList.clear()
                    if (foundTrack != null) {
                        trackList.addAll(foundTrack)
                        adapter.notifyDataSetChanged()
                        checkPlaceholder()
                        binding.rvTrack.visible()
                    }
                    if (errorMessage != null) {
                        showMessage(errorMessage)
                    }
                }
            }
        })
    }

    // Сообщение об ошибке - песня не нйдена или ошибка подключения
    private fun showMessage(text: String) {
        if (text.isNotEmpty()) {
            binding.progressBar.gone()
            binding.placeholder.visible()
            trackList.clear()
            adapter.notifyDataSetChanged()
            binding.placeholderMessage.text = text
            if (text == SEARCH_ERROR) {
                binding.imgSearchError.setImageResource(R.drawable.img_search_error)
                binding.btnReload.gone()
            } else {
                binding.imgSearchError.setImageResource(R.drawable.img_connection_error)
                binding.btnReload.visible()
            }
        } else {
            binding.placeholderMessage.gone()
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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val EMPTY_STRING: String = ""
        private const val SEARCH_ERROR = "Ничего не нашлось"
    }
}
