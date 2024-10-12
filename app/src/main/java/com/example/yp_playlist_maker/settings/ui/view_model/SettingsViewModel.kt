package com.example.yp_playlist_maker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.settings.domain.api.AppThemeInteractor
import com.example.yp_playlist_maker.settings.domain.models.AppThemeParams
import com.example.yp_playlist_maker.sharing.domain.api.interactor.ContactSupportInteractor
import com.example.yp_playlist_maker.sharing.domain.api.interactor.LicenseAgreementInteractor
import com.example.yp_playlist_maker.sharing.domain.api.interactor.ShareAppInteractor

class SettingsViewModel(
    private val appTheme: AppThemeInteractor,
    private val shareApp: ShareAppInteractor,
    private val contactSupport: ContactSupportInteractor,
    private val licenseAgreement: LicenseAgreementInteractor
) : ViewModel() {

    fun getAppTheme() : Boolean {
        return appTheme.getAppTheme()
    }

    fun saveAppTheme(checked: Boolean) {
        appTheme.saveAppTheme(AppThemeParams(checked))
    }

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