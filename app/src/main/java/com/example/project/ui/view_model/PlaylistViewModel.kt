package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.data.network.DatabaseMock
import com.example.project.domain.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlaylistViewModel : ViewModel() {

    private val database = DatabaseMock(scope = viewModelScope)

    val playlists: Flow<List<Playlist>> = flow {
        val collected = mutableListOf<Playlist>()

        database.getAllPlaylists().collect { playlist ->
            if (playlist != null) {
                collected.add(playlist)
                emit(collected.toList())
            }
        }
    }

    fun createNewPlaylist(name: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            database.addNewPlaylist(namePlaylist = name, description = description)
        }
    }
}