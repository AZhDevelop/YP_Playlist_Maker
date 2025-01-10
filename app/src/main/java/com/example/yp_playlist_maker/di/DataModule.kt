package com.example.yp_playlist_maker.di

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.os.Environment
import androidx.core.content.IntentCompat
import androidx.room.Room
import com.example.yp_playlist_maker.database.data.AppDatabase
import com.example.yp_playlist_maker.database.data.dao.PlaylistDao
import com.example.yp_playlist_maker.database.data.dao.TrackDao
import com.example.yp_playlist_maker.search.data.network.NetworkClient
import com.example.yp_playlist_maker.search.data.network.RetrofitNetworkClient
import com.example.yp_playlist_maker.search.data.network.TrackApi
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Converter
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

private const val INTENT_PUTTED_TRACK: String = "PuttedTrack"
private const val TRACK_LIST_KEY: String = "track_list_key"
private const val THEME_PREFERENCES: String = "theme_preferences"

val dataModule = module {

    single<Gson> { Gson() }

    single<TrackApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApi::class.java)
    }

    single<ConnectivityManager> {
        androidContext()
            .applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    single<SharedPreferences>(named(TRACK_LIST_KEY)) {
        androidContext().getSharedPreferences(TRACK_LIST_KEY, Context.MODE_PRIVATE)
    }

    single<SharedPreferences>(named(THEME_PREFERENCES)) {
        androidContext().getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            connectivityManager = get(),
            trackApi = get()
        )
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    factory<Track?> { (intent : Intent) ->
        IntentCompat.getParcelableExtra(intent, INTENT_PUTTED_TRACK, Track::class.java)
    }

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<TrackDao> { get<AppDatabase>().trackDao() }
    single<PlaylistDao> { get<AppDatabase>().playlistDao() }

    single<File> {
        File(androidContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists_covers")
    }

    single<ContentResolver> {
        androidContext().contentResolver
    }

}