package com.compose.tools

import androidx.compose.ui.graphics.Color
import com.compose.db.entity.MovieEntity
import com.compose.network.model.response.movie.popular.PopularMoviesResponse


val String.color
    get() = Color(android.graphics.Color.parseColor(this))

fun PopularMoviesResponse.toMovieEntities(): List<MovieEntity> {
    val entityList = ArrayList<MovieEntity>()
    results?.forEach {
        it.id?.let { nonNullId ->
            entityList.add(
                MovieEntity(
                    id = nonNullId,
                    genreIds = it.genreIds?.joinToString(",").orEmpty(),
                    originalTitle = it.originalTitle.orEmpty(),
                    releaseDate = it.releaseDate.orEmpty(),
                    title = it.title.orEmpty(),
                    voteAverage = it.voteAverage ?: 0.0f,
                    posterPath = it.posterPath.orEmpty()
                )
            )
        }
    }
    return entityList
}
