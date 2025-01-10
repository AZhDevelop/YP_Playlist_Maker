package com.example.yp_playlist_maker.playlist.view_model

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.models.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val playlistsDirectory: File,
    private val contentResolver: ContentResolver
): ViewModel() {

    fun createPlaylist() {
        viewModelScope.launch {
            playlistsInteractor.insertPlaylist(
                Playlist(
                    playlistId = 0,
                    playlistName = "TestName",
                    playlistDescription = "TestDescription",
                    playlistCoverPath = "TestPath",
                    trackIdList = "TestTrackList",
                    playlistSize = "TestPlaylistSize"
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
            val fileName = fileCount?.plus(1)
            val file = File(playlistsDirectory, "${fileName}.jpg")
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        }
    }

}