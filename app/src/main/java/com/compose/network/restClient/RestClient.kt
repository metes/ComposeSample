package com.compose.network.restClient

import com.compose.BuildConfig
import com.compose.network.model.response.movie.popular.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query



interface RestClient {

//    @GET("/4/list/{list_id}")
//    suspend fun getList(
//        @Query("api_key") apiKey: String = BuildConfig.MOVIE_API_KEY,
//        @Query("language") language: String = BuildConfig.LANGUAGE,
//        @Query("page") page: String = BuildConfig.PAGE_1
//    ): PopularMoviesResponse


    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.MOVIE_API_KEY,
        @Query("language") language: String = BuildConfig.LANGUAGE,
        @Query("page") page: Int// = BuildConfig.PAGE_1
    ): PopularMoviesResponse


    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = BuildConfig.MOVIE_API_KEY,
        @Query("language") language: String = BuildConfig.LANGUAGE,
        @Query("page") page: Int// = BuildConfig.PAGE_1
    ): PopularMoviesResponse


    @GET("/3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = BuildConfig.MOVIE_API_KEY,
        @Query("language") language: String = BuildConfig.LANGUAGE,
        @Query("page") page: Int// = BuildConfig.PAGE_1
    ): PopularMoviesResponse

}
