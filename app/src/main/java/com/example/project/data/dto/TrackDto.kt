package com.example.project.data.dto

data class TrackDto(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val image: String = "",
    var favorite: Boolean = false,
    var playlistId: Long = 0
)