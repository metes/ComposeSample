package com.compose.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.compose.db.dao.MovieDao
import com.compose.db.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
//@TypeConverters(Converters::class)
abstract class MovieDB : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {

        const val dbName = "movieDB"

        @Volatile
        private var instance: MovieDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MovieDB::class.java,
            dbName
        ).fallbackToDestructiveMigration().build()
    }

}
