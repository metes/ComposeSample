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
        resultStatus: (APIResultStatus<PopularMoviesResponse>) -> Unit,
      
        listType: ListType,
        page: Int
    ): List<MovieEntity> {
        return when (listType) {
            ListType.Popular -> fetchPopularMovies(resultStatus, listType.name, page)
            ListType.TopRated -> fetchTopRatedMovies(resultStatus, listType.name, page)
            ListType.Upcoming -> fetchUpcomingMovies(resultStatus, listType.name, page)
        }
    }

    private suspend fun fetchPopularMovies(
        resultStatus: (APIResultStatus<PopularMoviesResponse>) -> Unit,
        listTypeName: String,
        page: Int,
    ): List<MovieEntity> {
        val pageDataFromDB = movieDao.getPage(listTypeName, page)
        if (pageDataFromDB.isNullOrEmpty().not()) {
            return pageDataFromDB
        }

        apiRequester.sendRequest({ retrofitClient.getPopularMovies(page = page) }, {
            it.onSuccess { response ->
                val entityList = response?.toMovieEntities(listTypeName).orEmpty()

//                movieDao.nukeTable(listTypeName)

                movieDao.insertAll(entityList)
            }.onIdle {
                resultStatus(APIResultStatus.Idle())
            }.onLoading {
                resultStatus(APIResultStatus.Loading())
            }.onGeneralException { exception ->
                resultStatus(APIResultStatus.GeneralException(exception))
            }.onHTTPException { exception ->
                resultStatus(APIResultStatus.HTTPException(exception))
            }
        })

        return movieDao.getPage(listTypeName, page)
    }

    private suspend fun fetchTopRatedMovies(
        resultStatus: (APIResultStatus<PopularMoviesResponse>) -> Unit,
        listTypeName: String,
        page: Int
    ): List<MovieEntity> {

        apiRequester.sendRequest({ retrofitClient.getTopRatedMovies(page = page) }, {
            it.onSuccess { response ->
                val entityList = response?.toMovieEntities(listTypeName).orEmpty()

                movieDao.nukeTable(listTypeName)
                movieDao.insertAll(entityList)
            }.onIdle {
                resultStatus(APIResultStatus.Idle())
            }.onLoading {
                resultStatus(APIResultStatus.Loading())
            }.onGeneralException { exception ->
                resultStatus(APIResultStatus.GeneralException(exception))
            }.onHTTPException { exception ->
                resultStatus(APIResultStatus.HTTPException(exception))
            }
        })

        return movieDao.getAll(listTypeName)
    }

    private suspend fun fetchUpcomingMovies(
        resultStatus: (APIResultStatus<PopularMoviesResponse>) -> Unit,
        listTypeName: String,
        page: Int
    ): List<MovieEntity> {

        apiRequester.sendRequest({ retrofitClient.getUpcomingMovies(page = page) }, {
            it.onSuccess { response ->
                val entityList = response?.toMovieEntities(listTypeName).orEmpty()

                movieDao.nukeTable(listTypeName)
                movieDao.insertAll(entityList)
            }.onIdle {
                resultStatus(APIResultStatus.Idle())
            }.onLoading {
                resultStatus(APIResultStatus.Loading())
            }.onGeneralException { exception ->
                resultStatus(APIResultStatus.GeneralException(exception))
            }.onHTTPException { exception ->
                resultStatus(APIResultStatus.HTTPException(exception))
            }
        })

        return movieDao.getAll(listTypeName)
    }

}