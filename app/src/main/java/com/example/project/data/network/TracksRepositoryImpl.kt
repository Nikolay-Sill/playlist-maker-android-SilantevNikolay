package com.example.project.data.network

import com.example.project.data.database.AppDatabase
import com.example.project.data.database.mapper.TrackMapper.toEntity
import com.example.project.data.database.mapper.TrackMapper.toTrack
import com.example.project.data.dto.TracksSearchRequest
import com.example.project.data.dto.TracksSearchResponse
import com.example.project.domain.NetworkClient
import com.example.project.domain.Track
import com.example.project.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    database: AppDatabase
) : TracksRepository {
    private val tracksDao = database.tracksDao()

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return when (response.resultCode) {
            200 -> (response as TracksSearchResponse).results.map {
                Track(
                    id = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(it.trackTimeMillis),
                    image = it.artworkUrl100?.replace("100x100bb", "512x512bb")
                )
            }

            -1 -> throw IOException("network_error")
            else -> throw IOException("server_error")
        }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return tracksDao.getTrackByNameAndArtist(track.trackName, track.artistName)
            .map { entity -> entity?.toTrack() }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return tracksDao.getFavoriteTracks()
            .map { entities -> entities.map { it.toTrack() } }
    }

    override suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {
        tracksDao.insertTrack(
            track.copy(playlistId = playlistId).toEntity()
        )
    }

    override suspend fun deleteSongFromPlaylist(track: Track) {
        tracksDao.removeTrackFromPlaylist(track.id, track.playlistId)
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        tracksDao.insertTrack(
            track.copy(favorite = isFavorite).toEntity()
        )
    }

    override suspend fun getTracksByPlaylistId(playlistId: Long): List<Track> {
        return emptyList()
    }

    override fun getTracksByPlaylistIdFlow(playlistId: Long): Flow<List<Track>> {
        return tracksDao.getTracksByPlaylistId(playlistId)
            .map { entities -> entities.map { it.toTrack() } }
    }
}