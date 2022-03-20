package com.compose.ui.screens.movieList

import androidx.paging.PagingData
import com.compose.db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MovieItemUiStateData(
    val imdbId: String,
    val title: String,
    val year: Int,
    var bookmarked: Boolean = false
)


sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class ListRefreshing(val isRefreshing: Boolean) : UiState()
    data class GeneralException(val exception: Exception?) : UiState()
    data class MovieListScreenUiState(
        val isSignedIn: Boolean = false,
        var movieList: Flow<PagingData<MovieEntity>> = flowOf()
    ) : UiState()
}