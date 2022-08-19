package com.compose.ui.screens.movieList.uistate

import androidx.paging.PagingData
import com.compose.db.entity.MovieEntity
import com.compose.ui.screens.movieList.ListType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object MovieScreenUiState {

    sealed class MovieList {

        object Idle : MovieScreenUiState.MovieList()

        object Loading : MovieScreenUiState.MovieList()

        data class ListRefreshing(
            val isRefreshing: Boolean,
            var movieList: Flow<PagingData<MovieEntity>> = flowOf()
        ) : MovieScreenUiState.MovieList()

        data class GeneralException(val exception: Exception? = null) : MovieScreenUiState.MovieList()

        data class MovieListScreenMovieList(
            val isSignedIn: Boolean = false,
            val currentListType: ListType
        ) : MovieScreenUiState.MovieList()
    }

    sealed class DetailDialog {

        object Idle : MovieScreenUiState.DetailDialog()

        data class DetailDialog(var movie: MovieEntity) : MovieScreenUiState.DetailDialog()
    }
}