package com.example.yp_playlist_maker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val trackUrl: String = "https://itunes.apple.com"
    private var savedSearchText: String = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(trackUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create(TrackApi::class.java)
    private val trackList = ArrayList<Track>()
    private val adapter = TrackAdapter()

    private lateinit var editText: EditText
    private lateinit var clearText: ImageView
    private lateinit var backButton: ImageView
    private lateinit var recyclerViewTrack: RecyclerView
    private lateinit var placeholder: LinearLayout
    private lateinit var placeholderMessage: TextView
    private lateinit var imageViewError: ImageView
    private lateinit var reloadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
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

        placeholder.visibility = View.GONE
        reloadButton.visibility = View.GONE

        adapter.data = trackList

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        reloadButton.setOnClickListener {
            search()
        }

        // RecyclerView для списка песен
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)
        recyclerViewTrack.adapter = adapter

        clearText.visibility = View.INVISIBLE

        backButton.setOnClickListener {
            finish()
        }

        // Очищаем строку и список песен при нажатии на кнопку
        clearText.setOnClickListener {
            trackList.clear()
            adapter.notifyDataSetChanged()
            editText.setText("")
            it.hideKeyboard()
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
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        editText.addTextChangedListener(simpleTextWatcher)
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
        outState.putString(getString(R.string.saved_text), savedSearchText)
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
        trackService.search(editText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                placeholder.visibility = View.GONE
                            }
                            if (trackList.isEmpty()) {
                                showMessage(
                                    getString(R.string.nothing_found),
                                    "",
                                    R.drawable.img_search_error,
                                    false
                                )
                            }
                        }

                        else -> showMessage(
                            getString(R.string.connection_error),
                            "",
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

    // Плейсхолдер
    private fun showMessage(
        text: String,
        additionalMessage: String,
        image: Int,
        showButton: Boolean
    ) {
        if (text.isNotEmpty()) {
            placeholder.visibility = View.VISIBLE
            trackList.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            imageViewError.setImageResource(image)
            if (showButton) {
                reloadButton.visibility = View.VISIBLE
            } else {
                reloadButton.visibility = View.GONE
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }
}