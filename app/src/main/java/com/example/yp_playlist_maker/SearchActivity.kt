package com.example.yp_playlist_maker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private var savedSearchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val clearText = findViewById<ImageView>(R.id.iw_clear)
        val editText = findViewById<EditText>(R.id.et_search)
        val backButton = findViewById<ImageView>(R.id.iw_back)

        clearText.visibility = View.INVISIBLE

        backButton.setOnClickListener {
            finish()
        }

        // Очищаем строку при нажатии на кнопку
        clearText.setOnClickListener {
            editText.setText("")
            it.hideKeyboard()
        }


        // Проверяем сохраненное состояние текста и востанавллиевам, если что-то сохранено
        if (savedInstanceState != null) {
            savedSearchText = savedInstanceState.getString(getString(R.string.saved_text), savedSearchText)
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

}