package com.compose.di

import com.compose.network.client.APIClient
import com.compose.network.requester.ApiRequester
import com.compose.ui.screen.movieList.MovieListRepo
import com.google.gson.Gson
import org.koin.dsl.module

object KoinModule {

    val activityModule = module {
        single { Gson() }
        single { APIClient() }
        single { ApiRequester() }
        single { MovieListRepo() }
    }
}