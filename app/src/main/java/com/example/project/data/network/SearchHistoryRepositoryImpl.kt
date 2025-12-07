package com.example.project.data.network

import com.example.project.data.database.AppDatabase
import com.example.project.data.database.mapper.WordMapper.toEntity
import com.example.project.data.database.mapper.WordMapper.toWord
import com.example.project.domain.SearchHistoryRepository
import com.example.project.domain.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchHistoryRepositoryImpl(
    database: AppDatabase
) : SearchHistoryRepository {
    private val searchHistoryDao = database.searchHistoryDao()

    override suspend fun addToHistory(word: Word) {
        searchHistoryDao.insertWord(word.toEntity())
    }

    override fun getHistory(): Flow<List<Word>> {
        return searchHistoryDao.getHistory()
            .map { entities -> entities.map { it.toWord() } }
    }
}