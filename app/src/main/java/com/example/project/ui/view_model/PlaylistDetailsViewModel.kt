package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.domain.Playlist
import com.example.project.domain.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistId: Long,
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlist.asStateFlow()

    private val _isLoading = MutableStateFlow(true)

    init {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            _isLoading.value = true
            playlistsRepository.getPlaylist(playlistId).collect { playlist ->
                _playlist.value = playlist
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadPlaylist()
    }

    fun addTrackToPlaylist(track: com.example.project.domain.Track) {
        viewModelScope.launch {
            playlistsRepository.getPlaylist(playlistId).first()?.let { playlist ->

            }
        }
    }
}