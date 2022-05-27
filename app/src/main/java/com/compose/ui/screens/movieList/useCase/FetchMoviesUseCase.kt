package com.compose.ui.screens.movieList.useCase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.compose.db.dao.MovieDao
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.movieDetail.MovieDetailsResponse
import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import com.compose.network.requester.*
import com.compose.tools.toMovieEntities
import com.compose.ui.screens.movieList.ListType
import com.compose.ui.screens.movieList.MoviePagingSource
import com.compose.ui.screens.movieList.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchMoviesUseCase : KoinComponent {

    private val apiRequester by inject<ApiRequester>()
    private val movieDao by inject<MovieDao>()

    private suspend fun fetchMovieListByType(
        page: Int,
        listType: ListType,
        resultFunction: (APIResultStatus<PopularMoviesResponse?>) -> Unit,
    ): List<MovieEntity> {
        val resultFunction: suspend (apiResultStatus: APIResultStatus<PopularMoviesResponse>) -> Unit =
            {
                it.onSuccess { response ->
                    val entityList = response?.toMovieEntities(listType.name).orEmpty()
                    movieDao.insertAll(entityList)
                }.onIdle {
                    resultFunction(APIResultStatus.Idle())
                }.onLoading {
                    resultFunction(APIResultStatus.Loading())
                }.onGeneralException { exception ->
                    resultFunction(APIResultStatus.GeneralException(exception))
                }.onHTTPException { exception ->
                    resultFunction(APIResultStatus.HTTPException(exception))
                }
            }

        return when (listType) {
            ListType.Popular -> fetchPopularMovies(listType.name, page, resultFunction)
            ListType.TopRated -> fetchTopRatedMovies(listType.name, page, resultFunction)
            ListType.Upcoming -> fetchUpcomingMovies(listType.name, page, resultFunction)
        }
    }

    private suspend fun fetchPopularMovies(
        listTypeName: String,
        page: Int,
        resultFunction: suspend (apiResultStatus: APIResultStatus<PopularMoviesResponse>) -> Unit,
    ): List<MovieEntity> {
        val pageDataFromDB = movieDao.getPage(listTypeName, page)
        if (pageDataFromDB.isEmpty().not()) {
            return pageDataFromDB
        }

        apiRequester.sendRequest({ retrofitClient.getPopularMovies(page = page) }, resultFunction)

        return movieDao.getPage(listTypeName, page)
    }

    private suspend fun fetchTopRatedMovies(
        listTypeName: String,
        page: Int,
        resultFunction: suspend (apiResultStatus: APIResultStatus<PopularMoviesResponse>) -> Unit
    ): List<MovieEntity> {

        apiRequester.sendRequest({ retrofitClient.getTopRatedMovies(page = page) }, resultFunction)

        return movieDao.getAll(listTypeName)
    }

    private suspend fun fetchUpcomingMovies(
        listTypeName: String,
        page: Int,
        resultFunction: suspend (apiResultStatus: APIResultStatus<PopularMoviesResponse>) -> Unit
    ): List<MovieEntity> {

        apiRequester.sendRequest({ retrofitClient.getUpcomingMovies(page = page) }, resultFunction)

        return movieDao.getAll(listTypeName)
    }

    fun getMovieDetails(
        scope: CoroutineScope,
        movieId: Int,
        resultStatus: MutableStateFlow<MovieDetailsResponse?>
    ) {
        scope.launch(Dispatchers.IO) {
            apiRequester.sendRequest({ retrofitClient.getMovieDetails(movieId = movieId) }) {
                it.onSuccess {
                    resultStatus.emit(it)
                }
            }
        }
    }

    fun getMovieList(
        scope: CoroutineScope,
        listType: ListType,
        _pagingData: MutableSharedFlow<PagingData<MovieEntity>>,
        _uiState: MutableStateFlow<UiState>
    ) {
        scope.launch(Dispatchers.IO) {
            Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = {
                    MoviePagingSource { pageIndex ->
                        fetchMovieListByType(
                            listType = listType,
                            page = pageIndex,
                            resultFunction = {
                                resultFunction(it, _uiState, scope)
                            }
                        )

                    }
                }
            ).flow.collect {
                _pagingData.emit(it)
            }
        }
    }

    private fun resultFunction(
        apiResultStatus: APIResultStatus<PopularMoviesResponse?>,
        _uiState: MutableStateFlow<UiState>,
        scope: CoroutineScope
    ) {
        when (apiResultStatus) {
            is APIResultStatus.Success -> {
                scope.launch(Dispatchers.IO) {
                    _uiState.emit(
                        UiState.MovieListScreenUiState(
                            isSignedIn = false,
                            movieList = apiResultStatus.data.getOrNull()
                        )
                    )
                }
            }
            is APIResultStatus.Loading -> {
                scope.launch(Dispatchers.IO) { _uiState.emit(UiState.Loading) }
            }
            is APIResultStatus.Idle -> {
                scope.launch(Dispatchers.IO) { _uiState.emit(UiState.Idle) }
            }
            else -> {
                scope.launch(Dispatchers.IO) { _uiState.emit(UiState.Error) }
            }
        }
    }
}