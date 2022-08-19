package com.compose.ui.screens.movieList.useCase

import com.compose.db.dao.MovieDao
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.movieDetail.MovieDetailsResponse
import com.compose.network.requester.APIResultStatus
import com.compose.network.requester.ApiRequester
import com.compose.tools.toMovieEntities
import com.compose.ui.screens.movieList.ListType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchMoviesUseCase : KoinComponent {

    private val apiRequester by inject<ApiRequester>()
    private val movieDao by inject<MovieDao>()

    suspend fun fetchMovies(
        listType: ListType,
        page: Int
    ): List<MovieEntity> {
        val response = apiRequester.sendRequest {
            when (listType) {
                ListType.Popular  -> getPopularMovies(page = page)
                ListType.TopRated -> getTopRatedMovies(page = page)
                ListType.Upcoming -> getUpcomingMovies(page = page)
            }
        }

        return if (response is APIResultStatus.Success) {
            val entityList = response.data.getOrNull()?.toMovieEntities(listType.name).orEmpty()

            entityList
        } else {
            movieDao.getPage(listType.name, page)
        }
    }


    suspend fun getMovieDetails(
        movieId: Int,
        onResultFetched: suspend (MovieDetailsResponse?) -> Unit
    ) {
        val response = apiRequester.sendRequest { getMovieDetails(movieId) }

        if (response is APIResultStatus.Success) {
            onResultFetched(response.data.getOrNull())
        } else {
            onResultFetched(null)
        }

    }




}