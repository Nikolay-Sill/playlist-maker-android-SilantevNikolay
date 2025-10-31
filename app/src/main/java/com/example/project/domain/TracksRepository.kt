package com.example.project.domain

import com.example.project.data.network.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}