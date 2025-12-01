package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project.data.network.DatabaseMock
import com.example.project.domain.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistId: Long
) : ViewModel() {

    private val database = DatabaseMock(scope = viewModelScope)

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlist

    init {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            database.getPlaylist(playlistId).collect { p ->
                _playlist.value = p
            }
        }
    }

    companion object {
        fun factory(id: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlaylistDetailsViewModel(id) as T
                }
            }
    }
}
