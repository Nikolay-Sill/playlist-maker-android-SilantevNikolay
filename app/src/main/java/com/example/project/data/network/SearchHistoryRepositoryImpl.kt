package com.example.project.data.network

import com.example.project.domain.SearchHistoryRepository
import com.example.project.domain.Word
import kotlinx.coroutines.CoroutineScope

class SearchHistoryRepositoryImpl(private val scope: CoroutineScope) : SearchHistoryRepository {
    private val database = DatabaseMock(scope = scope)

    override fun addToHistory(word: Word) {
        database.addToHistory(word = word)
    }

    override suspend fun getHistory(): List<Word> {
        return database.getHistory()
    }
}