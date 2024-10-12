package com.example.yp_playlist_maker.sharing.domain.impl

import com.example.yp_playlist_maker.sharing.domain.api.interactor.LicenseAgreementInteractor
import com.example.yp_playlist_maker.sharing.domain.api.repository.LicenseAgreementRepository

class LicenseAgreementInteractorImpl(private val repository: LicenseAgreementRepository): LicenseAgreementInteractor {

    override fun getLicenseAgreement() {
        repository.getLicenseAgreement()
    }

}