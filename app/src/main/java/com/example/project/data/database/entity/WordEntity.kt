package com.example.project.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class WordEntity(
    @PrimaryKey
    val word: String,
    val timestamp: Long = System.currentTimeMillis()
)