package com.compose.network.model.response.movie.movieDetail


import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("adult")
    val adult: Boolean? = false, // false
    @SerializedName("backdrop_path")
    val backdropPath: String? = "", // /6fA9nie4ROlkyZAUlgKNjGNCbHG.jpg
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection? = BelongsToCollection(),
    @SerializedName("budget")
    val budget: Int? = 0, // 185000000
    @SerializedName("genres")
    val genres: List<Genre>? = listOf(),
    @SerializedName("homepage")
    val homepage: String? = "", // https://www.warnerbros.com/movies/dark-knight/
    @SerializedName("id")
    val id: Int? = 0, // 155
    @SerializedName("imdb_id")
    val imdbİd: String? = "", // tt0468569
    @SerializedName("original_language")
    val originalLanguage: String? = "", // en
    @SerializedName("original_title")
    val originalTitle: String? = "", // The Dark Knight
    @SerializedName("overview")
    val overview: String? = "", // Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets. The partnership proves to be effective, but they soon find themselves prey to a reign of chaos unleashed by a rising criminal mastermind known to the terrified citizens of Gotham as the Joker.
    @SerializedName("popularity")
    val popularity: Double? = 0.0, // 65.464
    @SerializedName("poster_path")
    val posterPath: String? = "", // /qJ2tW6WMUDux911r6m7haRef0WH.jpg
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>? = listOf(),
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>? = listOf(),
    @SerializedName("release_date")
    val releaseDate: String? = "", // 2008-07-14
    @SerializedName("revenue")
    val revenue: Int? = 0, // 1004558444
    @SerializedName("runtime")
    val runtime: Int? = 0, // 152
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>? = listOf(),
    @SerializedName("status")
    val status: String? = "", // Released
    @SerializedName("tagline")
    val tagline: String? = "", // Welcome to a world without rules.
    @SerializedName("title")
    val title: String? = "", // The Dark Knight
    @SerializedName("video")
    val video: Boolean? = false, // false
    @SerializedName("vote_average")
    val voteAverage: Float? = 0f, // 8.5
    @SerializedName("vote_count")
    val voteCount: Int? = 0 // 27674
)