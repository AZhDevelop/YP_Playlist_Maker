package com.example.yp_playlist_maker.util

object State {

    //Enum класс для статуса поиска, чтобы не тащить String через все слои
    //с последующей инициализация нужных в UI
    enum class SearchState {
        LOADING,
        SUCCESS,
        CONNECTION_ERROR,
        SEARCH_ERROR,
        RESET
    }

    //Enum класс для состояния плеера
    enum class PlayerState {
        LOADING,
        PREPARED,
        COMPLETED,
        START,
        PAUSE,
    }

    enum class FragmentState {
        SUCCESS,
        ERROR
    }
}