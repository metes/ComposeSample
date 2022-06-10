package com.compose.network.model.response.movie.movieDetail


import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    @SerializedName("id")
    val id: Int? = 0, // 429
    @SerializedName("logo_path")
    val logoPath: String? = "", // /2Tc1P3Ac8M479naPp1kYT3izLS5.png
    @SerializedName("name")
    val name: String? = "", // DC Comics
    @SerializedName("origin_country")
    val originCountry: String? = "" // US
)