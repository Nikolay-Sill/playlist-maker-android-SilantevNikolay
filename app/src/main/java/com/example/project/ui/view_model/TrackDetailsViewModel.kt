package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.domain.Playlist
import com.example.project.domain.PlaylistsRepository
import com.example.project.domain.Track
import com.example.project.domain.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TrackDetailsViewModel(
    private val track: Track,
    private val tracksRepository: TracksRepository,
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {

    sealed class State {
        data class Content(val track: Track, val playlists: List<Playlist>) : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Content(track, emptyList()))
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            tracksRepository.getTrackByNameAndArtist(track).collectLatest { updated ->
                val currentPlaylists = (_state.value as State.Content).playlists
                if (updated != null) {
                    _state.value = State.Content(updated, currentPlaylists)
                }
            }
        }

        viewModelScope.launch {
            playlistsRepository.getAllPlaylists().collectLatest { list ->
                val currentTrack = (_state.value as State.Content).track
                _state.value = State.Content(currentTrack, list)
            }
        }
    }

    fun toggleFavorite() {
        val current = (_state.value as? State.Content)?.track ?: return

        val newFavorite = !current.favorite

        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(current, newFavorite)
        }
    }

    fun addTrackToPlaylist(playlistId: Long) {
        val current = (_state.value as? State.Content)?.track ?: return

        viewModelScope.launch {
            tracksRepository.insertSongToPlaylist(
                current.copy(playlistId = playlistId),
                playlistId
            )
        }
    }
}