package com.example.project.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylist(playlistId: Long): Flow<Playlist?>

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String? = null)

    suspend fun deletePlaylistById(id: Long)

    suspend fun removeTrackFromPlaylist(track: Track)
}