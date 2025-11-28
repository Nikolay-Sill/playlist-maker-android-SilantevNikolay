package com.example.project.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project.domain.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TrackDetailsViewModel(
    track: Track
) : ViewModel() {

    sealed class State {
        data class Content(val track: Track) : State()
    }

    private val _state = MutableStateFlow<State>(State.Content(track))
    val state: StateFlow<State> = _state
}

class TrackDetailsViewModelFactory(
    private val track: Track
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackDetailsViewModel(track) as T
    }
}
