package com.example.project.data.database.mapper

import com.example.project.data.database.entity.PlaylistEntity
import com.example.project.domain.Playlist
import com.example.project.domain.Track

object PlaylistMapper {
    fun PlaylistEntity.toPlaylist(tracks: List<Track> = emptyList()): Playlist {
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            tracks = tracks
        )
    }

    fun Playlist.toEntity(): PlaylistEntity {
        return PlaylistEntity(
            id = this.id,
            name = this.name,
            description = this.description
        )
    }
}