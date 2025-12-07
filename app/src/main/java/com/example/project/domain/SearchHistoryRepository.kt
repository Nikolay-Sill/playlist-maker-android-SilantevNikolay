package com.example.project.domain

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun addToHistory(word: Word)
    fun getHistory(): Flow<List<Word>>
}