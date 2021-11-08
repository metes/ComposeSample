package com.compose.network.restClient

import com.compose.network.model.movie.MovieModel
import retrofit2.http.GET

interface RestClient {

   // /?=get-popular-movies&page=1&year=2020
    @GET("topMovies")
   suspend fun getPopularMovies(
    ): MovieModel


}
