package com.compose.network.restClient

import com.compose.BuildConfig
import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import retrofit2.http.GET

interface RestClient {

    @GET("/3/movie/popular?language=en-US&page=1&api_key=${BuildConfig.MOVIE_API_KEY}")
    suspend fun getPopularMovies(): PopularMoviesResponse

}
