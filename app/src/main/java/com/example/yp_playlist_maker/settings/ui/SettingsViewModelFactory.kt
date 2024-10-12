package com.example.yp_playlist_maker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yp_playlist_maker.creator.Creator

class SettingsViewModelFactory(): ViewModelProvider.Factory {

    private val shareApp = Creator.provideShareAppInteractor()
    private val contactSupport = Creator.provideContactSupportInteractor()
    private val licenseAgreement = Creator.provideLicenseAgreementInteractor()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(shareApp, contactSupport, licenseAgreement) as T
    }

}