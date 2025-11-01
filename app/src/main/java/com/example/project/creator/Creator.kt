package com.example.project.creator

import com.example.project.data.network.RetrofitNetworkClient
import com.example.project.data.network.TracksRepositoryImpl
import com.example.project.domain.TracksRepository

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(Storage()))
    }
}