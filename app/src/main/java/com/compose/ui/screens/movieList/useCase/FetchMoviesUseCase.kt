package com.compose.ui.screens.movieList.useCase

import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import com.compose.network.requester.APIResultStatus
import com.compose.network.requester.ApiRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchMoviesUseCase : KoinComponent {

    private val apiRequester by inject<ApiRequester>()

    fun fetchMovies(
        viewModelScope: CoroutineScope,
        function: (resultStatus: APIResultStatus<PopularMoviesResponse>) -> Unit
    ) {
        viewModelScope.launch {
            apiRequester.sendRequest({ retrofitClient.getPopularMovies() }, {
                function(it)
            })
        }
    }

}