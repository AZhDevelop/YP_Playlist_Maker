package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get())
    }

}