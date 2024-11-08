package com.example.yp_playlist_maker.util

object Constants {

    //Базовые константы
    const val EMPTY_STRING: String = ""
    const val DASH = "-"
    const val TRACK_HISTORY_SIZE: Int = 10
    const val ZERO_INDEX: Int = 0
    const val INTENT_PUTTED_TRACK: String = "PuttedTrack"

    //Константы для плеера
    const val DEFAULT_TIME: String = "00:00"
    const val PLAYER_IMAGE_RADIUS: Int = 8
    const val ALPHA_25 = 0.25F
    const val ALPHA_100 = 1F

    // Константы для SharedPreferences истории поиска треков
    const val TRACK_LIST_KEY: String = "track_list_key"
    const val TRACK_KEY: String = "track_key"

    // Константы для SharedPreferences Темы приложения
    const val THEME_PREFERENCES: String = "theme_preferences"
    const val THEME_PREFERENCES_KEY: String = "theme_preferences_key"

    //Enum класс для статуса поиска, чтобы не тащить String через все слои
    //с последующей инициализация нужных в UI
    enum class SearchState {
        LOADING,
        SUCCESS,
        CONNECTION_ERROR,
        SEARCH_ERROR
    }

    //Enum класс для состояния плеера
    enum class PlayerState {
        LOADING,
        PREPARED,
        COMPLETED,
        START,
        PAUSE,
    }
}