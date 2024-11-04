package com.example.yp_playlist_maker.util

object Constants {

    //Базовые константы
    const val EMPTY_STRING: String = ""
    const val TRACK_HISTORY_SIZE: Int = 10
    const val ZERO_INDEX: Int = 0

    // Константы для SharedPreferences
    const val TRACK_LIST_KEY: String = "track_list_key"
    const val TRACK_KEY: String = "track_key"

    //Enum класс для статуса поиска, чтобы не тащить String через все слои
    //с последующей инициализация нужных в UI
    enum class SearchStatus {
        LOADING,
        SUCCESS,
        CONNECTION_ERROR,
        SEARCH_ERROR
    }
}