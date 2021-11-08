package com.compose.di

import com.compose.network.client.APIClient
import com.compose.network.requester.ApiRequester
import com.compose.ui.MainViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModule {

    val activityModule = module {
        single { Gson() }
        single { APIClient() }
        single { ApiRequester() }
        viewModel { MainViewModel(get()) }
    }
}