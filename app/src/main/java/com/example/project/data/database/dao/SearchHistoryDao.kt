package com.example.project.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project.data.database.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
    fun getHistory(): Flow<List<WordEntity>>

    @Query("DELETE FROM search_history WHERE word = :word")
    suspend fun deleteWord(word: String)
}