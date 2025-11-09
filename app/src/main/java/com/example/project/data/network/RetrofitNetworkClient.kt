package com.example.project.data.network

import com.example.project.creator.Storage
import com.example.project.data.dto.TracksSearchRequest
import com.example.project.data.dto.TracksSearchResponse
import com.example.project.domain.NetworkClient

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(dto: Any): TracksSearchResponse {
        val searchList = storage.search((dto as TracksSearchRequest).expression)
        return TracksSearchResponse(searchList).apply { resultCode = 200 }
    }
}