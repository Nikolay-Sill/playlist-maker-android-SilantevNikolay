package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.domain.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
) : ViewModel() {

    private var _coverImageUri = MutableStateFlow<String?>(null)

    val coverImageUri: StateFlow<String?> = _coverImageUri.asStateFlow()

    fun setCoverImageUri(uri: String?) {
        _coverImageUri.value = uri
    }

    fun createNewPlaylist(name: String, description: String, coverImageUri: String? = null) {
        viewModelScope.launch {
            playlistsRepository.addNewPlaylist(name, description, coverImageUri)
            _coverImageUri.value = null
        }
    }
}