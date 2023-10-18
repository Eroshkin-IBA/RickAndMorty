package com.example.rickandmorty.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rickandmorty.dao.entity.CharacterEntity
import com.example.rickandmorty.dao.entity.EpisodeEntity
import com.example.rickandmorty.dao.entity.LocationEntity

@Database(
    entities = [CharacterEntity::class, EpisodeEntity::class, LocationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDataBase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}