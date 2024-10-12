package com.example.yp_playlist_maker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.sharing.domain.api.repository.LicenseAgreementRepository

class LicenseAgreementRepositoryImpl(private val context: Context) : LicenseAgreementRepository{

    override fun getLicenseAgreement() {
        val licenseAgreementIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(context.getString(R.string.license_agreement_link))
        }

        licenseAgreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(licenseAgreementIntent)
    }

}