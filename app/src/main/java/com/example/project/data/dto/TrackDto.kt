package com.example.project.data.dto

data class TrackDto(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val previewUrl: String?,
    val image: String?
)