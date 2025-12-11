package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.domain.Playlist
import com.example.project.domain.PlaylistsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistsRepository.getAllPlaylists().collect { playlists ->
                _playlists.value = playlists
            }
        }
    }

    fun deletePlaylist(id: Long) {
        viewModelScope.launch {
            playlistsRepository.deletePlaylistById(id)
        }
    }
}