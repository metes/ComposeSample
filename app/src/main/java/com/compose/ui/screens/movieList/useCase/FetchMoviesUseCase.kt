package com.compose.ui.screens.movieList.useCase

import com.compose.network.model.movie.MovieModel
import com.compose.network.requester.APIResultStatus
import com.compose.network.requester.ApiRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchMoviesUseCase: KoinComponent {

    private val apiRequester by inject<ApiRequester>()

    fun fetchMovies(viewModelScope: CoroutineScope,
                    function: (apiResultStatus: APIResultStatus<MovieModel>) -> Unit
    ) {
        viewModelScope.launch {
            val currencyListFlow =
                MutableStateFlow<APIResultStatus<MovieModel>>(APIResultStatus.Idle())

            apiRequester.sendRequest(currencyListFlow) { client ->
                client.retrofitClient.getPopularMovies()
            }

            currencyListFlow.collect { apiResult ->
                function(apiResult)
            }

        }
    }

}