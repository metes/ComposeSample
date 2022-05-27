package com.compose.ui.screens.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.movieDetail.MovieDetailsResponse
import com.compose.ui.screens.movieList.useCase.FetchMoviesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

enum class ListType {
    Popular, TopRated, Upcoming
}

class MovieListViewModel : ViewModel(), KoinComponent {

    private val moviesUseCase by inject<FetchMoviesUseCase>()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _onMovieDetail = MutableStateFlow<MovieDetailsResponse?>(null)
    val onMovieDetail = _onMovieDetail.asStateFlow()

    private val _pagingData = MutableSharedFlow<PagingData<MovieEntity>>()
    val pagingData = _pagingData.asSharedFlow()

    init {
        getMovieList(ListType.Popular)
    }

    fun getMovieList(listType: ListType) {
        moviesUseCase.getMovieList(viewModelScope, listType, _pagingData, _uiState)
    }

    fun getMovieDetails(movieId: Int) {
        moviesUseCase.getMovieDetails(viewModelScope, movieId, _onMovieDetail)
    }

}
