package com.example.yp_playlist_maker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yp_playlist_maker.creator.Creator

class SettingsViewModelFactory: ViewModelProvider.Factory {

    private val appTheme = Creator.provideAppThemeInteractor()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(appTheme) as T
    }

}