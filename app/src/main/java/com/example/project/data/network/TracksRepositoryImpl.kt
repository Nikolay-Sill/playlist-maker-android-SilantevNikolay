package com.example.project.data.network

import com.example.project.data.dto.TracksSearchRequest
import com.example.project.data.dto.TracksSearchResponse
import com.example.project.domain.NetworkClient
import com.example.project.domain.Track
import com.example.project.domain.TracksRepository
import kotlinx.coroutines.delay

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        delay(1000)
        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map {
                val seconds = it.trackTimeMillis / 1000
                val minutes = seconds / 60
                val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
                Track(it.id, it.trackName, it.artistName, trackTime)
            }
        } else {
            emptyList()
        }
    }
}