package com.compose.network.model.response.movie.popular


import com.compose.network.model.response.ErrorResponse
import com.google.gson.annotations.SerializedName

class PopularMoviesResponse(
    @SerializedName("page")
    val page: Int? = 0, // 1
    @SerializedName("results")
    val results: List<Result>? = listOf(),
    @SerializedName("total_pages")
    val totalPages: Int? = 0, // 32015
    @SerializedName("total_results")
    val totalResults: Int? = 0 // 640300
): ErrorResponse() {
    class Result(
        @SerializedName("adult")
        val adult: Boolean? = false, // false
        @SerializedName("backdrop_path")
        val backdropPath: String? = "", // /c6H7Z4u73ir3cIoCteuhJh7UCAR.jpg
        @SerializedName("genre_ids")
        val genreIds: List<Int?>? = listOf(),
        @SerializedName("id")
        val id: Int? = 0, // 524434
        @SerializedName("original_language")
        val originalLanguage: String? = "", // en
        @SerializedName("original_title")
        val originalTitle: String? = "", // Eternals
        @SerializedName("overview")
        val overview: String? = "", // The Eternals are a team of ancient aliens who have been living on Earth in secret for thousands of years. When an unexpected tragedy forces them out of the shadows, they are forced to reunite against mankind’s most ancient enemy, the Deviants.
        @SerializedName("popularity")
        val popularity: Double? = 0.0, // 10277.072
        @SerializedName("poster_path")
        val posterPath: String? = "", // /b6qUu00iIIkXX13szFy7d0CyNcg.jpg
        @SerializedName("release_date")
        val releaseDate: String? = "", // 2021-11-03
        @SerializedName("title")
        val title: String? = "", // Eternals
        @SerializedName("video")
        val video: Boolean? = false, // false
        @SerializedName("vote_average")
        val voteAverage: Double? = 0.0, // 7.3
        @SerializedName("vote_count")
        val voteCount: Int? = 0 // 3218
    )
}