package com.example.yp_playlist_maker.util

sealed interface Resource {
    class Success<T>(val data: T): Resource
    class Error(val message: Constants.SearchStatus): Resource
}