package com.example.project.domain

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun getHistoryRequests(): Flow<List<Word>>
    fun addToHistory(word: Word)
    suspend fun getHistory(): List<Word>
}