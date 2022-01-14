package com.compose.ui.screens.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.network.model.movie.MovieModel
import com.compose.network.model.movie.MovieResult
import com.compose.network.requester.APIResultStatus
import com.compose.ui.screens.movieList.useCase.FetchMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MovieListViewModel: ViewModel(), KoinComponent {

    private val fetchMoviesUseCase by inject<FetchMoviesUseCase>()

    private val _movieListUiState = MutableStateFlow<UiState>(UiState.Idle)
    val movieListUiState = _movieListUiState.asStateFlow()

    var movieResult: List<MovieItemUiStateData> = emptyList()

    init {
        getMovieList()
    }

    fun getMovieList(isRefresh: Boolean = false) {
        fetchMoviesUseCase.fetchMovies(viewModelScope) {
            handleApiResult(it, isRefresh)
        }
    }

    private fun handleApiResult(apiResultStatus: APIResultStatus<MovieModel>, isRefresh: Boolean) {
        viewModelScope.launch {
            when (apiResultStatus) {
                is APIResultStatus.Idle -> {
                    _movieListUiState.emit(UiState.Idle)
                }
                is APIResultStatus.Loading -> {
                    val state = if (isRefresh) {
                        UiState.ListRefreshing(true)
                    } else {
                        UiState.Loading
                    }
                    _movieListUiState.emit(state)
                }
                is APIResultStatus.Success<MovieModel> -> {
                    if (isRefresh) {
                        _movieListUiState.emit(UiState.ListRefreshing(false))
                    }

                    val data = apiResultStatus.data.getOrNull()
                    movieResult = data?.movieResults.convertToMovieItemUiState()


                    _movieListUiState.emit(UiState.MovieListScreenUiState())
                }
                is APIResultStatus.GeneralException -> {
                    if (isRefresh) {
                        _movieListUiState.emit(UiState.ListRefreshing(false))
                    }

                    _movieListUiState.emit(UiState.GeneralException(apiResultStatus.exception))
                }
            }
        }
    }

    private fun List<MovieResult>?.convertToMovieItemUiState(): List<MovieItemUiStateData> {
        val returnList = ArrayList<MovieItemUiStateData>()
        this?.forEach {
            returnList.add(
                MovieItemUiStateData(
                    imdbId = it.imdbId,
                    title = it.title,
                    year = it.year
                )
            )
        }
        return returnList
    }

}
