package com.example.project.data.network

import com.example.project.domain.Playlist
import com.example.project.domain.Track
import com.example.project.domain.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseMock(private val scope: CoroutineScope) {

    private val historyList = mutableListOf<Word>()
    private val _historyUpdates = MutableSharedFlow<Unit>()
    private val playlists = mutableListOf<Playlist>()
    private val tracks = mutableListOf<Track>()

    fun getHistory(): List<Word> {
        return historyList.toList()
    }

    fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }

    fun addToHistory(word: Word) {
        historyList.remove(word)
        historyList.add(0, word)

        if (historyList.size > 10) {
            historyList.removeAt(historyList.lastIndex)
        }
        notifyHistoryChanged()
    }

    fun getAllPlaylists(): Flow<Playlist?> = flow {
        delay(500)
        playlists.forEach { playlist ->
            val playlistTracks = tracks.filter { track ->
                track.playlistId == playlist.id
            }
            val playlistWithFilteredTracks = playlist.copy(tracks = playlistTracks)

            emit(playlistWithFilteredTracks)
            delay(100)
        }
    }

    fun addNewPlaylist(namePlaylist: String, description: String) {
        playlists.add(
            Playlist(
                id = playlists.size.toLong() + 1,
                name = namePlaylist,
                description = description,
                tracks = emptyList()
            )
        )
    }

    fun getPlaylist(playlistId: Long): Flow<Playlist?> = flow {
        emit(playlists.find { it.id == playlistId })
    }

    fun insertPlaylist(playlist: Playlist) {}

    fun deletePlaylistById(id: Long) {
        playlists.removeIf { it.id == id }
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName })
    }

    fun insertTrack(track: Track) {
        tracks.removeIf { it.id == track.id }
        tracks.add(track)
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(300)
        val favorites = tracks.filter { it.favorite }
        emit(favorites)
    }
}