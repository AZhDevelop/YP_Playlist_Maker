package com.example.yp_playlist_maker.sharing.domain.api

import android.content.Intent

interface ShareAppRepository {

    fun share() : Intent

}