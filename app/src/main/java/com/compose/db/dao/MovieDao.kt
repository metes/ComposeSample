package com.compose.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.compose.db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM MovieEntity WHERE list_type LIKE :listType")
    suspend fun getAll(listType: String): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE list_type LIKE :listType AND page LIKE :page")
    suspend fun getPage(listType: String, page: Int): List<MovieEntity>

    @Query("SELECT * FROM movieentity WHERE id IN (:ids) AND list_type LIKE :listType")
    suspend fun loadAllByIds(listType: String, ids: IntArray): List<MovieEntity>

    @Query("SELECT * FROM movieentity WHERE title LIKE :title AND list_type LIKE :listType")
    suspend fun findByName(listType: String, title: String): List<MovieEntity>

    @Insert
    suspend fun insertAll(users: List<MovieEntity>)

    @Insert
    suspend fun insert(users: MovieEntity)

    @Delete
    suspend fun deleteAll(user: List<MovieEntity>)

    @Delete
    suspend fun delete(user: MovieEntity)

    @Query("DELETE FROM movieentity WHERE list_type LIKE :listType")
    suspend fun nukeTable(listType: String)
}