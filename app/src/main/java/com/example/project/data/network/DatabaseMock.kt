package com.example.project.data.network

import com.example.project.domain.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DatabaseMock(private val scope: CoroutineScope) {

    private val historyList = mutableListOf<Word>()
    private val _historyUpdates = MutableSharedFlow<Unit>()

    fun getHistoryRequests(): List<Word> {
        return historyList.toList()
    }

    fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }

    fun addToHistory(word: Word) {
        historyList.remove(word)
        historyList.add(0, word)

        if (historyList.size > 10) {
            historyList.removeAt(historyList.lastIndex)
        }
        notifyHistoryChanged()
    }
}