package com.compose.tools

import androidx.compose.ui.graphics.Color
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.movieDetail.MovieDetailsResponse
import com.compose.network.model.response.movie.popular.PopularMoviesResponse


val String.color : Color
    get() = Color(android.graphics.Color.parseColor(this))

fun PopularMoviesResponse.toMovieEntities(listTypeName: String, language: String = ""): List<MovieEntity> {
    val entityList = ArrayList<MovieEntity>()
    results?.forEach { result ->
        result.toMovieEntity(listTypeName, language, page.orEmpty())?.let {
            entityList.add(it)
        }
    }
    return entityList
}

fun PopularMoviesResponse.Result?.toMovieEntity(
    listTypeName: String = "",
    language: String = "",
    page: Int = -1
): MovieEntity? {
    return this?.let { result ->
        MovieEntity(
            id = result.id?: -1,
            genreIds = result.genreIds?.joinToString(",").orEmpty(),
            originalTitle = result.originalTitle.orEmpty(),
            releaseDate = result.releaseDate.orEmpty(),
            title = result.title.orEmpty(),
            voteAverage = result.voteAverage ?: 0.0f,
            posterPath = result.posterPath.orEmpty(),
            originalLang = result.originalLanguage.orEmpty(),
            listType = listTypeName,
            page = page,
            language = language
        )
    }?: run {
        null
    }
}


fun MovieDetailsResponse.toMovieEntity(
    listTypeName: String = "",
    language: String = "",
    page: Int = -1
): MovieEntity {
    return MovieEntity(
        id = id ?: -1,
        genreIds = genres?.map { it.id }?.joinToString(",").orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        voteAverage = voteAverage ?: 0.0f,
        posterPath = posterPath.orEmpty(),
        originalLang = originalLanguage.orEmpty(),
        listType = listTypeName,
        page = page,
        language = language
    )
}


private fun Int?.orEmpty(): Int {
    return this ?: 0
}
