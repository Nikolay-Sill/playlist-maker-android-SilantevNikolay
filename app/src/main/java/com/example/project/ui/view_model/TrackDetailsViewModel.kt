package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.domain.Track
import com.example.project.domain.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TrackDetailsViewModel(
    private val track: Track,
    private val tracksRepository: TracksRepository
) : ViewModel() {

    sealed class State {
        data class Content(val track: Track) : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Content(track))
    val state: StateFlow<State> = _state.asStateFlow()

    private val _isFavorite = MutableStateFlow(track.favorite)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        viewModelScope.launch {
            tracksRepository.getTrackByNameAndArtist(track).collectLatest { updated ->
                if (updated != null) {
                    _state.value = State.Content(updated)
                }
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

    fun addToPlaylist(playlistId: Long) {
        viewModelScope.launch {
            tracksRepository.insertSongToPlaylist(track, playlistId)
        }
    }
}