package com.compose.ui.screens.movieList.useCase

import com.compose.db.dao.MovieDao
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import com.compose.network.requester.*
import com.compose.tools.toMovieEntities
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchMoviesUseCase : KoinComponent {

    private val apiRequester by inject<ApiRequester>()
    private val movieDao by inject<MovieDao>()

    suspend fun fetchMovies(
        resultStatus: (APIResultStatus<PopularMoviesResponse>, Boolean) -> Unit,
        isRefresh: Boolean
    ): Flow<List<MovieEntity>> {

        apiRequester.sendRequest({ retrofitClient.getPopularMovies() }, {
            it.onSuccess { response ->
                val entityList = response?.toMovieEntities().orEmpty()

                movieDao.nukeTable()
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

        return movieDao.getAll()
    }

}