package com.example.yp_playlist_maker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.util.State

class PlaylistsFragmentViewModel: ViewModel() {

    private val fragmentState = MutableLiveData<State.FragmentState>()
    fun getFragmentState(): LiveData<State.FragmentState> = fragmentState

    init {
        fragmentState.value = State.FragmentState.ERROR
    }
}