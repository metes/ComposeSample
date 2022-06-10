package com.compose.ui.screens.movieList

import androidx.paging.PagingData
import com.compose.db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class ListRefreshing(
        val isRefreshing: Boolean,
        var movieList: Flow<PagingData<MovieEntity>> = flowOf()
    ) : UiState()

    data class GeneralException(val exception: Exception? = null) : UiState()
    data class MovieListScreenUiState(
        val isSignedIn: Boolean = false,
        var movieList: Flow<PagingData<MovieEntity>> = flowOf()
    ) : UiState()

    data class DetailDialog(
        var movie: MovieEntity,
        var movieList: Flow<PagingData<MovieEntity>> = flowOf()
    ) : UiState()
}