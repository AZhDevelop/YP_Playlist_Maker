package com.example.yp_playlist_maker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.sharing.domain.api.repository.ContactSupportRepository

class ContactSupportRepositoryImpl(private val context: Context) : ContactSupportRepository {

    override fun contactSupport() {
        val contactSupport = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse(context.getString(R.string.mailto))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.my_email)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.contact_support_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.contact_support_message))
        }
        contactSupport.addFlags(FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(contactSupport)
    }

}