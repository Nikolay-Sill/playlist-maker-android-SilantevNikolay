package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.domain.Playlist
import com.example.project.domain.PlaylistsRepository
import com.example.project.domain.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun getTotalDurationMinutes(tracks: List<Track>): Int {
        return tracks.sumOf { track ->
            track.trackTime
                .replace(":", ".")
                .split(".")
                .let { it[0].toInt() * 60 + it[1].toInt() }
        } / 60
    }

    fun getTracksCountText(count: Int): String {
        val mod10 = count % 10
        val mod100 = count % 100

        val word = when {
            mod100 in 11..14 -> "треков"
            mod10 == 1 -> "трек"
            mod10 in 2..4 -> "трека"
            else -> "треков"
        }

        return "$count $word"
    }
}