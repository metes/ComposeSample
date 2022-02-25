package com.compose.ui.screens.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import com.compose.network.requester.APIResultStatus
import com.compose.ui.screens.movieList.useCase.FetchMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

enum class ListType {
    Popular, TopRated, Upcoming
}

class MovieListViewModel : ViewModel(), KoinComponent {

    private val moviesUseCase by inject<FetchMoviesUseCase>()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        getMovieList()
    }

    fun getMovieList(isRefresh: Boolean = false, listType: ListType = ListType.Popular) {
        viewModelScope.launch(Dispatchers.IO) {
            moviesUseCase.fetchMovieListByType(::handleNotSuccessResults, isRefresh, listType)
                .collect { movieList ->
                    if (isRefresh) {
                        _uiState.emit(UiState.ListRefreshing(false))
                    }
                    _uiState.emit(UiState.MovieListScreenUiState(movieList = movieList))
                }
        }
    }

    private fun handleNotSuccessResults(
        apiResultStatus: APIResultStatus<PopularMoviesResponse>,
        isRefresh: Boolean
    ) {
        viewModelScope.launch {
            when (apiResultStatus) {
                is APIResultStatus.Idle -> _uiState.emit(UiState.Idle)
                is APIResultStatus.Loading -> {
                    val state = if (isRefresh) {
                        UiState.ListRefreshing(true)
                    } else {
                        UiState.Loading
                    }
                    _uiState.emit(state)
                }
                is APIResultStatus.GeneralException -> {
                    if (isRefresh) {
                        _uiState.emit(UiState.ListRefreshing(false))
                    }

                    _uiState.emit(UiState.GeneralException(apiResultStatus.exception))
                }
                else -> {
                    /** Dont anything */
                }
            }
        }
    }

//    private fun List<MovieResult>?.convertToMovieItemUiState(): List<MovieItemUiStateData> {
//        val returnList = ArrayList<MovieItemUiStateData>()
//        this?.forEach {
//            returnList.add(
//                MovieItemUiStateData(
//                    imdbId = it.imdbId,
//                    title = it.title,
//                    year = it.year
//                )
//            )
//        }
//        return returnList
//    }

}
