package com.example.project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project.domain.Track
import com.example.project.ui.activity.CreatePlaylistScreen
import com.example.project.ui.activity.FavoritesScreen
import com.example.project.ui.activity.PlaylistMakerScreen
import com.example.project.ui.activity.PlaylistsScreen
import com.example.project.ui.activity.SearchScreen
import com.example.project.ui.activity.SettingsScreen
import com.example.project.ui.activity.TrackDetailsScreen
import com.example.project.ui.activity.TrackDetailsScreenError
import com.example.project.ui.view_model.PlaylistViewModel
import com.example.project.ui.view_model.TrackDetailsViewModel

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object Playlists : Screen("playlists")
    object Favorites : Screen("favorites")
    object CreatePlaylist : Screen("create_playlist")
    object TrackDetails : Screen("track_details")
}


@Composable
fun PlaylistHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            PlaylistMakerScreen(
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onPlaylistsClick = { navController.navigate(Screen.Playlists.route) },
                onFavoritesClick = { navController.navigate(Screen.Favorites.route) }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("track", track)

                    navController.navigate(Screen.TrackDetails.route)
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Playlists.route) { entry ->
            val playlistViewModel: PlaylistViewModel = viewModel(entry)

            PlaylistsScreen(
                onBackClick = { navController.popBackStack() },
                onCreatePlaylistClick = { navController.navigate(Screen.CreatePlaylist.route) },
                playlistViewModel = playlistViewModel
            )
        }


        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.CreatePlaylist.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Playlists.route)
            }

            val playlistViewModel: PlaylistViewModel = viewModel(parentEntry)

            CreatePlaylistScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { name, desc ->
                    playlistViewModel.createNewPlaylist(name, desc)
                    navController.popBackStack()
                }
            )
        }





        composable(Screen.TrackDetails.route) {
            val track =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Track>("track")

            if (track == null) {
                TrackDetailsScreenError(
                    onBackClick = { navController.popBackStack() }
                )
                return@composable
            }

            val viewModel: TrackDetailsViewModel = viewModel(
                factory = TrackDetailsViewModel.getViewModelFactory(track)
            )

            TrackDetailsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}