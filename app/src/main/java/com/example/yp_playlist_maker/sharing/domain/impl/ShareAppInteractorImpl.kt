package com.example.yp_playlist_maker.sharing.domain.impl

import android.content.Intent
import com.example.yp_playlist_maker.sharing.domain.api.ShareAppInteractor
import com.example.yp_playlist_maker.sharing.domain.api.ShareAppRepository

class ShareAppInteractorImpl(private val repository: ShareAppRepository) : ShareAppInteractor {

    override fun share() : Intent {
        return repository.share()
    }

}