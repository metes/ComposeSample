package com.compose.network.model.response.movie.movieDetail


import com.compose.network.model.response.ErrorResponse
import com.google.gson.annotations.SerializedName

class MovieDetailsResponse(
    @SerializedName("adult")
    val adult: Boolean? = false, // false
    @SerializedName("backdrop_path")
    val backdropPath: String? = "", // /x747ZvF0CcYYTTpPRCoUrxA2cYy.jpg
    @SerializedName("belongs_to_collection")
    val belongsToCollection: Any? = Any(), // null
    @SerializedName("budget")
    val budget: Int? = 0, // 146000000
    @SerializedName("genres")
    val genres: List<Genre>? = listOf(),
    @SerializedName("homepage")
    val homepage: String? = "", // https://moonfall.movie
    @SerializedName("id")
    val id: Int? = 0, // 406759
    @SerializedName("imdb_id")
    val imdbId: String? = "", // tt5834426
    @SerializedName("original_language")
    val originalLanguage: String? = "", // en
    @SerializedName("original_title")
    val originalTitle: String? = "", // Moonfall
    @SerializedName("overview")
    val overview: String? = "", // A mysterious force knocks the moon from its orbit around Earth and sends it hurtling on a collision course with life as we know it.
    @SerializedName("popularity")
    val popularity: Double? = 0.0, // 4297.704
    @SerializedName("poster_path")
    val posterPath: String? = "", // /odVv1sqVs0KxBXiA8bhIBlPgalx.jpg
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>? = listOf(),
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>? = listOf(),
    @SerializedName("release_date")
    val releaseDate: String? = "", // 2022-02-03
    @SerializedName("revenue")
    val revenue: Int? = 0, // 30000000
    @SerializedName("runtime")
    val runtime: Int? = 0, // 130
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>? = listOf(),
    @SerializedName("status")
    val status: String? = "", // Released
    @SerializedName("tagline")
    val tagline: String? = "", // Humanity will face the dark side of the Moon.
    @SerializedName("title")
    val title: String? = "", // Moonfall
    @SerializedName("video")
    val video: Boolean? = false, // false
    @SerializedName("vote_average")
    val voteAverage: Double? = 0.0, // 6.5
    @SerializedName("vote_count")
    val voteCount: Int? = 0 // 657
): ErrorResponse()