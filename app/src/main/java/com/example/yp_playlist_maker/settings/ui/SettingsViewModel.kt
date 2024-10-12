package com.example.yp_playlist_maker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.sharing.domain.api.interactor.ContactSupportInteractor
import com.example.yp_playlist_maker.sharing.domain.api.interactor.LicenseAgreementInteractor
import com.example.yp_playlist_maker.sharing.domain.api.interactor.ShareAppInteractor

class SettingsViewModel(
    private val shareApp: ShareAppInteractor,
    private val contactSupport: ContactSupportInteractor,
    private val licenseAgreement: LicenseAgreementInteractor
) : ViewModel() {

    fun share() {
        shareApp.share()
    }

    fun contact() {
        contactSupport.contact()
    }

    fun getLicenseAgreement() {
        licenseAgreement.getLicenseAgreement()
    }
}