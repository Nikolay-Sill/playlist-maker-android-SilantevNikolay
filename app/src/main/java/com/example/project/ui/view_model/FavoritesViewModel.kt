package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.domain.Track
import com.example.project.domain.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val _favoriteList = MutableStateFlow<List<Track>>(emptyList())
    val favoriteList: StateFlow<List<Track>> = _favoriteList.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            tracksRepository.getFavoriteTracks().collect { favorites ->
                _favoriteList.value = favorites
            }
        }
    }

    fun toggleFavorite(track: Track, isFavorite: Boolean) {
        viewModelScope.launch {
            val updated = track.copy(favorite = isFavorite)
            tracksRepository.updateTrackFavoriteStatus(updated, isFavorite)
            tracksRepository.deleteIfNotFavoriteAndNotInPlaylist(updated)
        }
    }
}