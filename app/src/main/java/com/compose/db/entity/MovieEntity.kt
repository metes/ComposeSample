package com.compose.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "original_title") val originalTitle: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Float,
    @ColumnInfo(name = "genre_ids") val genreIds: String,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    @ColumnInfo(name = "original_lang") val originalLang: String,
    @ColumnInfo(name = "list_type") val listType: String,
    @ColumnInfo(name = "page") val page: Int,
    @ColumnInfo(name = "language") val language: String
)
