package com.example.yp_playlist_maker.util

object Constants {
    const val TRACK_LIST_KEY: String = "track_list_key"

    enum class SearchStatus {
        LOADING,
        SUCCESS,
        CONNECTION_ERROR,
        SEARCH_ERROR
    }
}