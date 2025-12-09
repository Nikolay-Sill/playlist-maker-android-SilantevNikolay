package com.example.project.di

import com.example.project.data.database.AppDatabase
import com.example.project.data.network.PlaylistsRepositoryImpl
import com.example.project.data.network.RetrofitNetworkClient
import com.example.project.data.network.SearchHistoryRepositoryImpl
import com.example.project.data.network.TracksRepositoryImpl
import com.example.project.data.preferences.SearchHistoryPreferences
import com.example.project.data.preferences.searchHistoryDataStore
import com.example.project.domain.NetworkClient
import com.example.project.domain.PlaylistsRepository
import com.example.project.domain.SearchHistoryRepository
import com.example.project.domain.TracksRepository
import com.example.project.domain.Track
import com.example.project.ui.view_model.FavoritesViewModel
import com.example.project.ui.view_model.PlaylistDetailsViewModel
import com.example.project.ui.view_model.PlaylistsViewModel
import com.example.project.ui.view_model.SearchViewModel
import com.example.project.ui.view_model.TrackDetailsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<NetworkClient> { RetrofitNetworkClient() }

    single { AppDatabase.getInstance(androidContext()) }

    single<TracksRepository> {
        TracksRepositoryImpl(
            networkClient = get(),
            database = get()
        )
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(
            database = get()
        )
    }

    single {
        SearchHistoryPreferences(
            dataStore = androidContext().searchHistoryDataStore
        )
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            preferences = get()
        )
    }

    viewModel { PlaylistsViewModel(playlistsRepository = get()) }

    viewModel { (playlistId: Long) ->
        PlaylistDetailsViewModel(
            playlistId = playlistId,
            playlistsRepository = get()
        )
    }

    viewModel {
        SearchViewModel(
            tracksRepository = get(),
            searchHistoryRepository = get()
        )
    }

    viewModel { (track: Track) ->
        TrackDetailsViewModel(
            track = track,
            tracksRepository = get(),
            playlistsRepository = get()
        )
    }

    viewModel {
        FavoritesViewModel(
            tracksRepository = get()
        )
    }
}