package com.compose.ui.screens.movieList.useCase

import com.compose.db.dao.MovieDao
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import com.compose.network.requester.*
import com.compose.tools.toMovieEntities
import com.compose.ui.screens.movieList.ListType
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchMoviesUseCase : KoinComponent {

    private val apiRequester by inject<ApiRequester>()
    private val movieDao by inject<MovieDao>()

    suspend fun fetchMovieListByType(
        resultStatus: (APIResultStatus<PopularMoviesResponse>, Boolean) -> Unit,
        isRefresh: Boolean,
        listType: ListType
    ): Flow<List<MovieEntity>> {
        return when (listType) {
            ListType.Popular -> fetchPopularMovies(resultStatus, isRefresh, listType.name)
            ListType.TopRated -> fetchTopRatedMovies(resultStatus, isRefresh, listType.name)
            ListType.Upcoming -> fetchUpcomingMovies(resultStatus, isRefresh, listType.name)
        }
    }

    private suspend fun fetchPopularMovies(
        resultStatus: (APIResultStatus<PopularMoviesResponse>, Boolean) -> Unit,
        isRefresh: Boolean,
        listTypeName: String
    ): Flow<List<MovieEntity>> {

        apiRequester.sendRequest({ retrofitClient.getPopularMovies() }, {
            it.onSuccess { response ->
                val entityList = response?.toMovieEntities(listTypeName).orEmpty()

                movieDao.nukeTable(listTypeName)
                movieDao.insertAll(entityList)
            }.onIdle {
                resultStatus(APIResultStatus.Idle(), isRefresh)
            }.onLoading {
                resultStatus(APIResultStatus.Loading(), isRefresh)
            }.onGeneralException { exception ->
                resultStatus(APIResultStatus.GeneralException(exception), isRefresh)
            }.onHTTPException { exception ->
                resultStatus(APIResultStatus.HTTPException(exception), isRefresh)
            }
        })

        return movieDao.getAll(listTypeName)
    }

    private suspend fun fetchTopRatedMovies(
        resultStatus: (APIResultStatus<PopularMoviesResponse>, Boolean) -> Unit,
        isRefresh: Boolean,
        listTypeName: String
    ): Flow<List<MovieEntity>> {

        apiRequester.sendRequest({ retrofitClient.getTopRatedMovies() }, {
            it.onSuccess { response ->
                val entityList = response?.toMovieEntities(listTypeName).orEmpty()

                movieDao.nukeTable(listTypeName)
                movieDao.insertAll(entityList)
            }.onIdle {
                resultStatus(APIResultStatus.Idle(), isRefresh)
            }.onLoading {
                resultStatus(APIResultStatus.Loading(), isRefresh)
            }.onGeneralException { exception ->
                resultStatus(APIResultStatus.GeneralException(exception), isRefresh)
            }.onHTTPException { exception ->
                resultStatus(APIResultStatus.HTTPException(exception), isRefresh)
            }
        })

        return movieDao.getAll(listTypeName)
    }


    private suspend fun fetchUpcomingMovies(
        resultStatus: (APIResultStatus<PopularMoviesResponse>, Boolean) -> Unit,
        isRefresh: Boolean,
        listTypeName: String
    ): Flow<List<MovieEntity>> {

        apiRequester.sendRequest({ retrofitClient.getUpcomingMovies() }, {
            it.onSuccess { response ->
                val entityList = response?.toMovieEntities(listTypeName).orEmpty()

                movieDao.nukeTable(listTypeName)
                movieDao.insertAll(entityList)
            }.onIdle {
                resultStatus(APIResultStatus.Idle(), isRefresh)
            }.onLoading {
                resultStatus(APIResultStatus.Loading(), isRefresh)
            }.onGeneralException { exception ->
                resultStatus(APIResultStatus.GeneralException(exception), isRefresh)
            }.onHTTPException { exception ->
                resultStatus(APIResultStatus.HTTPException(exception), isRefresh)
            }
        })

        return movieDao.getAll(listTypeName)
    }

}