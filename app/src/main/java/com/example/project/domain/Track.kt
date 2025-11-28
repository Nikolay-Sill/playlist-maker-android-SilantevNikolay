package com.example.project.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String = "",
    var favorite: Boolean = false,
    var playlistId: Long = 0
) : Parcelable