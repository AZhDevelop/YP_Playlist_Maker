package com.example.yp_playlist_maker.media.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.FavouriteTracksInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.State
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FavouritesFragmentViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor
): ViewModel() {

    private val fragmentState = MutableLiveData<State.FragmentState>()
    fun getFragmentState(): LiveData<State.FragmentState> = fragmentState

    private val favouriteTracksList = MutableLiveData<List<Track>>()
    fun getFavouriteTracksList(): LiveData<List<Track>> = favouriteTracksList

//    init {
//        fragmentState.value = State.FragmentState.ERROR
//        checkFavouriteTrackList()
//    }

    fun checkFavouriteTrackList() {
        viewModelScope.launch {
            favouriteTracksInteractor
                .getFavouriteTracks()
                .collect{ favouriteTrackList ->
                    Log.d("log", "Tracks: ${favouriteTrackList.size}")
                    if (favouriteTrackList.isEmpty()) {
                        fragmentState.value = State.FragmentState.ERROR
                        favouriteTracksList.value = favouriteTrackList
                    } else {
                        fragmentState.value = State.FragmentState.SUCCESS
                        favouriteTracksList.value = favouriteTrackList
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}