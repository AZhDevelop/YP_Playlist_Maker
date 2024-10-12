package com.example.yp_playlist_maker.sharing.data.impl

import android.content.Context
import android.content.Intent
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.sharing.domain.api.repository.ShareAppRepository

class ShareAppRepositoryImpl(private val context: Context): ShareAppRepository {

    override fun share() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_link))
        }

        val createChooser = Intent.createChooser(shareIntent, context.getString(R.string.share_with))
        createChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(createChooser)
    }

}