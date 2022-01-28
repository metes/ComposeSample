package com.compose.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.compose.db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM MovieEntity")
    fun getAll(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movieentity WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movieentity WHERE title LIKE :title")
    fun findByName(title: String): Flow<List<MovieEntity>>

    @Insert
    fun insertAll(users: List<MovieEntity>)

    @Insert
    fun insert(users: MovieEntity)

    @Delete
    fun deleteAll(user: List<MovieEntity>)

    @Delete
    fun delete(user: MovieEntity)

    @Query("DELETE FROM movieentity")
    fun nukeTable()
}