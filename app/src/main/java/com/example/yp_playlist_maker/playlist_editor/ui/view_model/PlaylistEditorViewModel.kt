package com.example.yp_playlist_maker.playlist_editor.ui.view_model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.util.Converter
import com.example.yp_playlist_maker.util.State
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PlaylistEditorViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val playlistsDirectory: File,
    private val contentResolver: ContentResolver
): ViewModel() {

    private var fileName: Int? = null
    private val playlistEditorState =  MutableLiveData<State.PlaylistEditorState>()
    fun getPlaylistEditorState(): LiveData<State.PlaylistEditorState> = playlistEditorState

    fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
    ) {
        viewModelScope.launch {
            playlistsInteractor.insertPlaylist(
                Playlist(
                    playlistId = 0,
                    playlistName = playlistName,
                    playlistDescription = playlistDescription,
                    playlistCoverPath = if (fileName == null) {
                        "null"
                    } else {
                        "${playlistsDirectory.path}/${fileName}.jpg"
                    },
                    playlistSize = "0",
                    playlistDuration = "0"
                )
            )
        }
    }

    fun updatePlaylist(
        playlist: Playlist,
        playlistName: String,
        playlistDescription: String,
        isCoverUpdated: Boolean
    ) {
        viewModelScope.launch {
            playlistsInteractor.updatePlaylist(
                Playlist(
                    playlistId = playlist.playlistId,
                    playlistName = playlistName,
                    playlistDescription = playlistDescription,
                    playlistCoverPath =
                    if (isCoverUpdated) {
                        "${playlistsDirectory.path}/${fileName}.jpg"
                    } else {
                        playlist.playlistCoverPath
                    },
                    playlistSize = playlist.playlistSize,
                    playlistDuration = playlist.playlistDuration
                )
            )
        }
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        viewModelScope.launch {
            if (!playlistsDirectory.exists()){
                playlistsDirectory.mkdirs()
            }
            val fileCount = playlistsDirectory.listFiles()?.size
            fileName = fileCount?.plus(1)
            val file = File(playlistsDirectory, "${fileName}.jpg")
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        }
    }

    fun checkPlaylist(playlist: Playlist?) {
        if (playlist != null) {
            playlistEditorState.value = State.PlaylistEditorState.EDITOR
            fileName
        } else {
            playlistEditorState.value = State.PlaylistEditorState.CREATOR
        }
    }

    fun getRoundedCorners(playerImageRadius: Int): Int {
        return Converter.dpToPx(playerImageRadius)
    }

}