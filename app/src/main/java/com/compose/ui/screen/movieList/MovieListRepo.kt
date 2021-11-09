package com.compose.ui.screen.movieList

import com.compose.network.model.movie.MovieModel
import com.compose.network.requester.ApiRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MovieListRepo: KoinComponent {

    private val apiRequester by inject<ApiRequester>()

    private val _currencyListFlow = MutableStateFlow<ApiRequester.APIResult<MovieModel>>(
        ApiRequester.APIResult.Idle()
    )
    val currencyListFlow = _currencyListFlow.asStateFlow()

    fun getCurrencyList(scope: CoroutineScope) {
        scope.launch {
            apiRequester.sendRequest(_currencyListFlow) { client ->
                client.retrofitClient.getPopularMovies()
            }
        }
    }
}