package com.compose.ui.screens.movieList.useCase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.compose.db.dao.MovieDao
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.movieDetail.MovieDetailsResponse
import com.compose.network.requester.APIResultStatus
import com.compose.network.requester.ApiRequester
import com.compose.tools.toMovieEntities
import com.compose.ui.screens.movieList.ListType
import com.compose.ui.screens.movieList.MoviePagingSource
import com.compose.ui.screens.movieList.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchMoviesUseCase : KoinComponent {

    private val apiRequester by inject<ApiRequester>()
    private val movieDao by inject<MovieDao>()

    fun getMovieList(
        scope: CoroutineScope,
        listType: ListType,
        _pagingData: MutableSharedFlow<PagingData<MovieEntity>>,
        _uiState: MutableStateFlow<UiState>
    ) {
        scope.launch(Dispatchers.IO) {
            _uiState.emit(UiState.Loading)

            Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = {
                    MoviePagingSource { pageIndex ->
                        fetchMovies(listType = listType, page = pageIndex, uiState = _uiState)
                    }
                }
            ).flow.collect {
                _pagingData.emit(it)
            }
        }
    }

    fun getMovieDetails(
        scope: CoroutineScope,
        movieId: Int,
        resultStatus: MutableStateFlow<MovieDetailsResponse?>
    ) {
        scope.launch(Dispatchers.IO) {
            val response = apiRequester.sendRequest { getMovieDetails(movieId) }

            if (response is APIResultStatus.Success) {
                resultStatus.emit(response.data.getOrNull())
            } else {
                resultStatus.emit(null)
            }
        }
    }

    private suspend fun fetchMovies(
        listType: ListType,
        page: Int,
        uiState: MutableStateFlow<UiState>
    ): List<MovieEntity> {
        val response = apiRequester.sendRequest {
            when (listType) {
                ListType.Popular  -> getPopularMovies(page = page)
                ListType.TopRated -> getTopRatedMovies(page = page)
                ListType.Upcoming -> getUpcomingMovies(page = page)
            }
        }

        uiState.emit(UiState.MovieListScreenUiState())

        return if (response is APIResultStatus.Success) {
            val entityList = response.data.getOrNull()?.toMovieEntities(listType.name).orEmpty()
//            movieDao.insertAll(entityList)

            entityList
        } else {
            movieDao.getPage(listType.name, page)
        }
    }


}