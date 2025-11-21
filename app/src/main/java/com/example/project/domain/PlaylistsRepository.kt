package com.example.project.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylist(playlistId: Long): Flow<Playlist?>

    fun getAllPlaylists(): Flow<Playlist?>

    suspend fun addNewPlaylist(id: Long, name: String, description: String, tracks: List<Track>)

    suspend fun deletePlaylistById(id: Long)
}