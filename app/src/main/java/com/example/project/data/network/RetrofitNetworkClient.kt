package com.example.project.data.network

import com.example.project.data.dto.BaseResponse
import com.example.project.data.dto.TracksSearchRequest
import com.example.project.domain.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApiService::class.java)

    override suspend fun doRequest(dto: Any): BaseResponse {
        return try {
            when (dto) {
                is TracksSearchRequest -> {
                    val response = iTunesService.searchTracks(dto.expression)
                    response.resultCode = 200
                    response
                }

                else -> BaseResponse().apply { resultCode = 400 }
            }
        } catch (_: Exception) {
            BaseResponse().apply {
                resultCode = -1
            }
        }
    }
}
