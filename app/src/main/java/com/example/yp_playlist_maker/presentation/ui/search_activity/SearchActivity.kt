package com.example.yp_playlist_maker.presentation.ui.search_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.yp_playlist_maker.util.Creator
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.domain.api.interactor.TrackInteractor
import com.example.yp_playlist_maker.domain.models.Track
import com.example.yp_playlist_maker.presentation.ui.application.gone
import com.example.yp_playlist_maker.presentation.ui.application.invisible
import com.example.yp_playlist_maker.presentation.ui.application.visible
import com.example.yp_playlist_maker.presentation.ui.audio_player_activity.AudioPlayerActivity
import com.example.yp_playlist_maker.presentation.ui.track.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private val trackService = Creator.provideTrackInteractor()

    private var savedSearchText: String = EMPTY_STRING
    private var trackList: ArrayList<Track> = arrayListOf()
    private val adapter = TrackAdapter()

    private lateinit var editText: EditText
    private lateinit var clearText: ImageView
    private lateinit var backButton: ImageView
    private lateinit var recyclerViewTrack: RecyclerView
    private lateinit var placeholder: LinearLayout
    private lateinit var placeholderMessage: TextView
    private lateinit var imageViewError: ImageView
    private lateinit var reloadButton: Button
    private lateinit var searchTextMessage: TextView
    private lateinit var clearHistoryButton: Button
    private val trackHistoryList: ArrayList<Track> = arrayListOf()
    private var updateTrackHistory: Boolean = false
    private lateinit var progressBar: ProgressBar
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { if (editText.text.isNotEmpty()) { search() } }
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {

        val trackHistoryInteractor = Creator.provideSeacrhHistoryInteractor()
        val trackHistory = trackHistoryInteractor.getHistory()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        clearText = findViewById(R.id.iw_clear)
        editText = findViewById(R.id.et_search)
        backButton = findViewById(R.id.iw_back)
        recyclerViewTrack = findViewById(R.id.rv_track)
        placeholder = findViewById(R.id.placeholder)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        imageViewError = findViewById(R.id.img_search_error)
        reloadButton = findViewById(R.id.btn_reload)
        searchTextMessage = findViewById(R.id.tv_search_history)
        clearHistoryButton = findViewById(R.id.btn_clear_history)
        progressBar = findViewById(R.id.progressBar)

        clearText.invisible()
        placeholder.gone()
        reloadButton.gone()
        clearHistoryButton.gone()
        searchTextMessage.gone()
        recyclerViewTrack.gone()

        adapter.data = trackList

        // RecyclerView для списка песен
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)
        recyclerViewTrack.adapter = adapter

        if (trackHistory.isNotEmpty()) {
            trackList.addAll(trackHistory)
            trackHistoryList.addAll(trackHistory)
            adapter.notifyDataSetChanged()
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && editText.text.isEmpty() && trackList.isNotEmpty()) {
                enableSearchHistoryVisibility()
            }
        }

        // Логика запуска поиска песен с клавиатуры
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkPlaceholder()
                search()
            }
            false
        }

        // Кнопка перезапуска поиска песен, если отсутствовал интернет
        reloadButton.setOnClickListener {
            checkPlaceholder()
            search()
        }

        // Закрываем Activity
        backButton.setOnClickListener {
            finish()
        }

        // Очищаем строку и список песен при нажатии на кнопку
        clearText.setOnClickListener {
            trackList.clear()
            adapter.notifyDataSetChanged()
            editText.setText(EMPTY_STRING)
            it.hideKeyboard()
        }

        clearHistoryButton.setOnClickListener {
            trackHistoryInteractor.clearHistory()
            trackList.clear()
            trackHistoryList.clear()
            adapter.notifyDataSetChanged()
            disableSearchHistoryVisibility()
        }

        // Проверяем сохраненное состояние текста и востанавллиевам, если что-то сохранено
        if (savedInstanceState != null) {
            savedSearchText =
                savedInstanceState.getString(getString(R.string.saved_text), savedSearchText)
            editText.setText(savedSearchText)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearText.visibility = clearButtonVisibility(s)
                savedSearchText = editText.text.toString()
                disableSearchHistoryVisibility()
                if (editText.text.isEmpty() && trackHistoryList.isNotEmpty()) {
                    trackList.clear()
                    trackList.addAll(trackHistoryList)
                    adapter.notifyDataSetChanged()
                    enableSearchHistoryVisibility()
                }
                if (editText.text.isEmpty()) {
                    checkPlaceholder()
                }
                if (editText.text.isNotEmpty()) {
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        editText.addTextChangedListener(simpleTextWatcher)

        adapter.onTrackClick = {
            if (clickDebounce()) {
                trackHistoryInteractor.saveClickedTrack(it, trackHistoryList)
                val displayAudioPlayer = Intent(this, AudioPlayerActivity::class.java)
                displayAudioPlayer.apply {
                    putExtra(AudioPlayerActivity.INTENT_PUTTED_TRACK, it)
                }
                startActivity(displayAudioPlayer)
                if (editText.text.isEmpty()) {
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

        progressBar.visible()

        trackService.searchTrack(editText.text.toString(), object : TrackInteractor.TrackConsumer {
            override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                handler.post {
                    progressBar.gone()
                    trackList.clear()
                    if (foundTrack != null) {
                        trackList.addAll(foundTrack)
                        adapter.notifyDataSetChanged()
                        checkPlaceholder()
                        recyclerViewTrack.visible()
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
            progressBar.gone()
            placeholder.visible()
            trackList.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (text == SEARCH_ERROR) {
                imageViewError.setImageResource(R.drawable.img_search_error)
                reloadButton.gone()
            } else {
                imageViewError.setImageResource(R.drawable.img_connection_error)
                reloadButton.visible()
            }
        } else {
            placeholderMessage.gone()
        }
    }

    private fun enableSearchHistoryVisibility() {
        searchTextMessage.visible()
        clearHistoryButton.visible()
        recyclerViewTrack.visible()
    }

    private fun disableSearchHistoryVisibility() {
        searchTextMessage.gone()
        clearHistoryButton.gone()
        recyclerViewTrack.gone()
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
        if (placeholder.isVisible) {
            placeholder.gone()
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val EMPTY_STRING: String = ""
        private const val SEARCH_ERROR = "Ничего не нашлось"
    }
}
