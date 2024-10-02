package com.example.yp_playlist_maker.presentation.search_activity

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.SearchHistory
import com.example.yp_playlist_maker.domain.models.Track
import com.example.yp_playlist_maker.presentation.track.TrackAdapter
import com.example.yp_playlist_maker.TrackResponse
import com.example.yp_playlist_maker.TrackService
import com.example.yp_playlist_maker.presentation.application.EMPTY_STRING
import com.example.yp_playlist_maker.presentation.application.TRACK_KEY
import com.example.yp_playlist_maker.presentation.application.TRACK_LIST_KEY
import com.example.yp_playlist_maker.presentation.application.gone
import com.example.yp_playlist_maker.presentation.application.invisible
import com.example.yp_playlist_maker.presentation.application.visible
import com.example.yp_playlist_maker.presentation.audio_player_activity.AudioPlayerActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var savedSearchText: String = EMPTY_STRING
    private var trackList: ArrayList<Track> = arrayListOf()
    private val adapter = TrackAdapter()
    private val trackService = TrackService().trackService
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
    private val gson: Gson = Gson()
    private var updateTrackHistory: Boolean = false
    private lateinit var progressBar: ProgressBar
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { if (editText.text.isNotEmpty()) { search() } }
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val sharedPreferences = getSharedPreferences(TRACK_LIST_KEY, MODE_PRIVATE)
        val trackHistory = sharedPreferences.getString(TRACK_KEY, EMPTY_STRING)

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

        if (trackHistory != "") {
            val trackHistoryJson = gson.fromJson(trackHistory, Array<Track>::class.java)
            trackList.addAll(trackHistoryJson)
            trackHistoryList.addAll(trackHistoryJson)
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
                search()
            }
            false
        }

        // Кнопка перезапуска поиска песен, если отсутствовал интернет
        reloadButton.setOnClickListener {
            search()
            placeholder.gone()
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
            sharedPreferences.edit()
                .clear()
                .apply()
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
                if (editText.text.isEmpty() && placeholder.visibility == View.VISIBLE) {
                    placeholder.gone()
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
                SearchHistory().saveClickedTrack(sharedPreferences, it, trackHistoryList, gson)
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

        trackService.search(editText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    progressBar.gone()
                    when (response.code()) {
                        200 -> {
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                placeholder.gone()
                                recyclerViewTrack.visible()
                            }
                            if (trackList.isEmpty()) {
                                showMessage(
                                    getString(R.string.nothing_found),
                                    EMPTY_STRING,
                                    R.drawable.img_search_error,
                                    false
                                )
                            }
                        }

                        else -> showMessage(
                            getString(R.string.connection_error),
                            EMPTY_STRING,
                            R.drawable.img_connection_error,
                            true
                        )

                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showMessage(
                        getString(R.string.connection_error),
                        t.message.toString(),
                        R.drawable.img_connection_error,
                        true
                    )
                }

            })
    }

    // Сообщение об ошибке - песня не нйдена или ошибка подключения
    private fun showMessage(
        text: String,
        additionalMessage: String,
        image: Int,
        showButton: Boolean
    ) {
        if (text.isNotEmpty()) {
            progressBar.gone()
            placeholder.visible()
            trackList.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            imageViewError.setImageResource(image)
            if (showButton) {
                reloadButton.visible()
            } else {
                reloadButton.gone()
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
        if (placeholder.visibility == View.VISIBLE) {
            placeholder.gone()
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
