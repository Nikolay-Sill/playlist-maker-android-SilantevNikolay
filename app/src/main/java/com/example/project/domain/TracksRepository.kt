package com.example.project.domain

import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>

    fun getTrackByNameAndArtist(track: Track): Flow<Track?>

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun insertSongToPlaylist(track: Track, playlistId: Long)

    suspend fun deleteSongFromPlaylist(track: Track)

    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)

    suspend fun getTracksByPlaylistId(playlistId: Long): List<Track>

    fun getTracksByPlaylistIdFlow(playlistId: Long): Flow<List<Track>>
}