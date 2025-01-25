package com.example.yp_playlist_maker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.util.State
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MediaPlaylistsFragmentViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private val fragmentState = MutableLiveData<State.FragmentState>()
    fun getFragmentState(): LiveData<State.FragmentState> = fragmentState

    private val playlistList = MutableLiveData<List<Playlist>>()
    fun getPlaylistList(): LiveData<List<Playlist>> = playlistList

    init {
        fragmentState.value = State.FragmentState.ERROR
        checkPlaylistList()
    }

    fun checkPlaylistList() {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylistList()
                .collect{ plList ->
                    if (plList.isEmpty()) {
                        fragmentState.value = State.FragmentState.ERROR
                        playlistList.value = plList
                    } else {
                        fragmentState.value = State.FragmentState.SUCCESS
                        playlistList.value = plList
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}