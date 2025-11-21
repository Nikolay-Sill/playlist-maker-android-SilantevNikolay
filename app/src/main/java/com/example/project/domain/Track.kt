package com.example.project.domain

data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String = "",
    var favorite: Boolean = false,
    var playlistId: Long = 0
)