package com.compose.network.model.movie


import com.google.gson.annotations.SerializedName

data class MovieResult(
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("year")
    val year: Int
)