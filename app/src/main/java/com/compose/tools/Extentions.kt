package com.compose.tools

import androidx.compose.ui.graphics.Color
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.popular.PopularMoviesResponse


val String.color : Color
    get() = Color(android.graphics.Color.parseColor(this))

fun PopularMoviesResponse.toMovieEntities(listTypeName: String, language: String = ""): List<MovieEntity> {
    val entityList = ArrayList<MovieEntity>()
    results?.forEach {
        it.id?.let { nonNullId ->
            entityList.add(
                MovieEntity(
                    id = nonNullId,
                    genreIds = it.genreIds?.joinToString(",").orEmpty(),
                    originalTitle = it.originalTitle.orEmpty(),
                    releaseDate = it.releaseDate.orEmpty(),
                    title = "${page.orEmpty()} - ${it.title.orEmpty()}",
                    voteAverage = it.voteAverage ?: 0.0f,
                    posterPath = it.posterPath.orEmpty(),
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
