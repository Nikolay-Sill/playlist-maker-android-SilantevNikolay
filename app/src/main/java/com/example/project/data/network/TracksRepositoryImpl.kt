package com.example.project.data.network

import com.example.project.data.dto.TracksSearchRequest
import com.example.project.data.dto.TracksSearchResponse
import com.example.project.domain.NetworkClient
import com.example.project.domain.Track
import com.example.project.domain.TracksRepository
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
//    private val scope: CoroutineScope
) : TracksRepository {
//    private val database = DatabaseMock(
//        scope = scope
//    )

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map {
                Track(
                    id = it.id,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(it.trackTimeMillis)
                )
            }
        } else {
            emptyList()
        }
    }

//    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
//        return database.getTrackByNameAndArtist(track)
//    }
//
//    override suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {
//        database.insertTrack(track.copy(playlistId = playlistId))
//    }
//
//    override suspend fun deleteSongFromPlaylist(track: Track) {
//        database.insertTrack(track.copy(playlistId = 0))
//    }
//
//    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
//        database.insertTrack(track.copy(favorite = isFavorite))
//    }
//
//    override fun getFavoriteTracks(): Flow<List<Track>> {
//        return database.getFavoriteTracks()
//    }
}