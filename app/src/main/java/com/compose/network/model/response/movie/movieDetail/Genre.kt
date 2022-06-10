package com.compose.network.model.response.movie.movieDetail


import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id: Int? = 0, // 18
    @SerializedName("name")
    val name: String? = "" // Drama
)