package com.compose.tools

import androidx.compose.ui.graphics.Color
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.popular.PopularMoviesResponse


val String.color : Color
    get() = Color(android.graphics.Color.parseColor(this))

fun PopularMoviesResponse.toMovieEntities(listTypeName: String, language: String = ""): List<MovieEntity> {
    val entityList = ArrayList<MovieEntity>()
    results?.forEachIndexed { index, result ->
        result.id?.let { nonNullId ->
            entityList.add(
                MovieEntity(
                    id = nonNullId,
                    genreIds = result.genreIds?.joinToString(",").orEmpty(),
                    originalTitle = result.originalTitle.orEmpty(),
                    releaseDate = result.releaseDate.orEmpty(),
                    title = result.title.orEmpty(),
                    voteAverage = result.voteAverage ?: 0.0f,
                    posterPath = result.posterPath.orEmpty(),
                    originalLang = result.originalLanguage.orEmpty(),
                    listType = listTypeName,
                    page = page.orEmpty(),
                    language = language
                )
            )
        }
    }
    return entityList
}

private fun Int?.orEmpty(): Int {
    return this ?: 0
}
