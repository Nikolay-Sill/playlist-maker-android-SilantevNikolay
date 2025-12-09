package com.example.project.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchHistoryPreferences(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope =
        CoroutineScope(CoroutineName("search-history-preferences") + SupervisorJob())
) {

    fun addEntry(word: String) {
        if (word.isBlank()) return

        coroutineScope.launch {
            dataStore.edit { preferences ->
                val historyString = preferences[KEY_HISTORY].orEmpty()

                val history = if (historyString.isNotEmpty()) {
                    historyString.split(SEPARATOR).toMutableList()
                } else {
                    mutableListOf()
                }

                history.remove(word)
                history.add(0, word)

                val updated = history
                    .take(MAX_ENTRIES)
                    .joinToString(SEPARATOR)

                preferences[KEY_HISTORY] = updated
            }
        }
    }

    suspend fun getEntries(): List<String> {
        val preferences = dataStore.data.first()
        val historyString = preferences[KEY_HISTORY].orEmpty()

        return if (historyString.isEmpty()) {
            emptyList()
        } else {
            historyString.split(SEPARATOR)
        }
    }

    private companion object {
        val KEY_HISTORY = stringPreferencesKey("search_history")
        const val MAX_ENTRIES = 10
        const val SEPARATOR = ","
    }
}
