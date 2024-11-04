package com.example.yp_playlist_maker.settings.ui.view_model

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.settings.domain.api.AppThemeInteractor
import com.example.yp_playlist_maker.settings.domain.models.AppThemeParams

class SettingsViewModel(
    private val appTheme: AppThemeInteractor,
) : ViewModel() {

    fun getAppTheme() : Boolean {
        return appTheme.getAppTheme()
    }

    fun saveAppTheme(checked: Boolean) {
        appTheme.saveAppTheme(AppThemeParams(checked))
    }

    fun share(context: Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_link))
        }

        val createChooser = Intent.createChooser(shareIntent, context.getString(R.string.share_with))
        createChooser.addFlags(FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(createChooser)
    }

    fun contact(context: Context) {
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

    fun getLicenseAgreement(context: Context) {
        val licenseAgreementIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(context.getString(R.string.license_agreement_link))
        }

        licenseAgreementIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(licenseAgreementIntent)
    }
}