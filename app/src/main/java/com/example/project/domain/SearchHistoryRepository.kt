package com.example.project.domain

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun addToHistory(word: Word)
    suspend fun getHistory(): List<Word>
}