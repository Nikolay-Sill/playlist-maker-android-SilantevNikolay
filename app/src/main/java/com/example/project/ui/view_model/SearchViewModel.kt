package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.domain.SearchHistoryRepository
import com.example.project.domain.Track
import com.example.project.domain.TracksRepository
import com.example.project.domain.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

@OptIn(ExperimentalStdlibApi::class, FlowPreview::class)
class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    private val _history = MutableStateFlow<List<com.example.project.domain.Word>>(emptyList())
    val history = _history.asStateFlow()

    private val _query = MutableStateFlow("")

    init {
        loadHistory()

        viewModelScope.launch {
            _query
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isEmpty()) {
                        clearSearch()
                    } else {
                        search(query)
                    }
                }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            searchHistoryRepository.getHistory().collect { historyList ->
                _history.value = historyList
            }
        }
    }

    fun onQueryChange(newValue: String) {
        _query.value = newValue
    }

    fun search(whatSearch: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.value = SearchState.Searching

                searchHistoryRepository.addToHistory(
                    Word(word = whatSearch)
                )

                val list = tracksRepository.searchTracks(expression = whatSearch)
                _searchScreenState.value = SearchState.Success(list = list)

            } catch (e: IOException) {
                _searchScreenState.value = SearchState.Fail(e.message.toString())
            }
        }
    }

    fun clearSearch() {
        _searchScreenState.value = SearchState.Initial
    }

    suspend fun getHistoryList(): List<com.example.project.domain.Word> {
        return searchHistoryRepository.getHistory().first()
    }
}

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val list: List<Track>) : SearchState()
    data class Fail(val error: String) : SearchState()
}