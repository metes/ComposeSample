package com.compose.network.model.response.movie.movieDetail


import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String? = "", // HK
    @SerializedName("name")
    val name: String? = "" // Hong Kong
)