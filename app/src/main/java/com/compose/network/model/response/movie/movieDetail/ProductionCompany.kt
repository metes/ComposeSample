package com.compose.network.model.response.movie.movieDetail


import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    @SerializedName("id")
    val id: Int? = 0, // 347
    @SerializedName("logo_path")
    val logoPath: Any? = Any(), // null
    @SerializedName("name")
    val name: String? = "", // Centropolis Entertainment
    @SerializedName("origin_country")
    val originCountry: String? = "" // US
)