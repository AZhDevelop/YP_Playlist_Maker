package com.example.yp_playlist_maker.sharing.domain.impl

import android.content.Intent
import com.example.yp_playlist_maker.sharing.domain.api.interactor.ContactSupportInteractor
import com.example.yp_playlist_maker.sharing.domain.api.repository.ContactSupportRepository

class ContactSupportInteractorImpl(private val repository: ContactSupportRepository) : ContactSupportInteractor {

    override fun contactSupport(): Intent {
        return repository.contactSupport()
    }

}