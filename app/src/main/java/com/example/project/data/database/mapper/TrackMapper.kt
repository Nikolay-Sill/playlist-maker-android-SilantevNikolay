package com.example.project.data.database.mapper

import com.example.project.data.database.entity.TrackEntity
import com.example.project.domain.Track

object TrackMapper {
    fun TrackEntity.toTrack(): Track {
        return Track(
            id = this.id,
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = this.trackTime,
            image = this.image,
            favorite = this.favorite,
            playlistId = this.playlistId
        )
    }

    fun Track.toEntity(): TrackEntity {
        return TrackEntity(
            id = this.id,
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = this.trackTime,
            image = this.image,
            favorite = this.favorite,
            playlistId = this.playlistId
        )
    }
}