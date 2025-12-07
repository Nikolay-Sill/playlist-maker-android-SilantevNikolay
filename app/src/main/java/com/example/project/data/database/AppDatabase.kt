package com.example.project.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project.data.database.dao.PlaylistDao
import com.example.project.data.database.dao.SearchHistoryDao
import com.example.project.data.database.dao.TracksDao
import com.example.project.data.database.entity.PlaylistEntity
import com.example.project.data.database.entity.TrackEntity
import com.example.project.data.database.entity.WordEntity

@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        WordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tracksDao(): TracksDao
    abstract fun playlistsDao(): PlaylistDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "playlist_maker_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}