package com.example.project.data.network

import com.example.project.data.preferences.SearchHistoryPreferences
import com.example.project.domain.SearchHistoryRepository
import com.example.project.domain.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRepositoryImpl(
    private val preferences: SearchHistoryPreferences
) : SearchHistoryRepository {

    override suspend fun addToHistory(word: Word) {
        preferences.addEntry(word.word)
    }

    override fun getHistory(): Flow<List<Word>> = flow {
        val entries = preferences.getEntries()
        emit(entries.map { Word(it) })
    }
}