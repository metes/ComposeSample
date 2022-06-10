package com.compose.network.model.response.movie.movieDetail


import com.google.gson.annotations.SerializedName

data class BelongsToCollection(
    @SerializedName("backdrop_path")
    val backdropPath: String? = "", // /xfKot7lqaiW4XpL5TtDlVBA9ei9.jpg
    @SerializedName("id")
    val id: Int? = 0, // 263
    @SerializedName("name")
    val name: String? = "", // The Dark Knight Collection
    @SerializedName("poster_path")
    val posterPath: String? = "" // /hGvOMQBD88jAV0olS2DT1AxreHn.jpg
)