package com.example.project.data.network

import com.example.project.domain.SearchHistoryRepository
import com.example.project.domain.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(private val scope: CoroutineScope) : SearchHistoryRepository {
    private val database = DatabaseMock(scope = scope)

    override suspend fun getHistoryRequests(): Flow<List<Word>> = flow {
        emit(database.getHistoryRequests())
    }

    override fun addToHistory(word: Word) {
        database.addToHistory(word = word)
    }

    override suspend fun getHistory(): List<Word> {
        return database.getHistoryRequests()
    }
}