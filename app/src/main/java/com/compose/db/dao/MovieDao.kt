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
    fun getAll(listType: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movieentity WHERE id IN (:ids) AND list_type LIKE :listType")
    fun loadAllByIds(listType: String, ids: IntArray): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movieentity WHERE title LIKE :title AND list_type LIKE :listType")
    fun findByName(listType: String, title: String): Flow<List<MovieEntity>>

    @Insert
    fun insertAll(users: List<MovieEntity>)

    @Insert
    fun insert(users: MovieEntity)

    @Delete
    fun deleteAll(user: List<MovieEntity>)

    @Delete
    fun delete(user: MovieEntity)

    @Query("DELETE FROM movieentity WHERE list_type LIKE :listType")
    fun nukeTable(listType: String)
}