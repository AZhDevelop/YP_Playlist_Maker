package com.example.yp_playlist_maker.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.example.yp_playlist_maker.search.data.network.NetworkClient
import com.example.yp_playlist_maker.search.data.network.RetrofitNetworkClient
import com.example.yp_playlist_maker.search.data.network.TrackApi
import com.example.yp_playlist_maker.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

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

    single<SharedPreferences>(named("trackHistoryRepository")) {
        androidContext().getSharedPreferences(Constants.TRACK_LIST_KEY, Context.MODE_PRIVATE)
    }

    single<SharedPreferences>(named("appThemeRepository")) {
        androidContext().getSharedPreferences(Constants.THEME_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            connectivityManager = get(),
            trackApi = get()
        )
    }

}