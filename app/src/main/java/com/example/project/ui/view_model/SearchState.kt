package com.example.project.ui.view_model

import com.example.project.creator.Storage
import com.example.project.data.network.RetrofitNetworkClient
import com.example.project.data.network.TracksRepositoryImpl
import com.example.project.domain.Track
import com.example.project.domain.TracksRepository

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val list: List<Track>) : SearchState()
    data class Fail(val error: String) : SearchState()
}

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(Storage()))
    }
}