package com.compose.network.model.movie


import com.google.gson.annotations.SerializedName

data class MovieModel(
    @SerializedName("movie_results")
    val movieResults: List<MovieResult>,
    @SerializedName("search_results")
    val searchResults: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("status_message")
    val statusMessage: String
)