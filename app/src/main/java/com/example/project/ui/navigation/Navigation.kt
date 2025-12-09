package com.example.project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project.domain.Track
import com.example.project.ui.activity.CreatePlaylistScreen
import com.example.project.ui.activity.FavoritesScreen
import com.example.project.ui.activity.PlaylistDetailsScreen
import com.example.project.ui.activity.PlaylistMakerScreen
import com.example.project.ui.activity.PlaylistsScreen
import com.example.project.ui.activity.SearchScreen
import com.example.project.ui.activity.SettingsScreen
import com.example.project.ui.activity.TrackDetailsScreen
import com.example.project.ui.view_model.FavoritesViewModel
import com.example.project.ui.view_model.PlaylistDetailsViewModel
import com.example.project.ui.view_model.PlaylistsViewModel
import com.example.project.ui.view_model.SearchViewModel
import com.example.project.ui.view_model.TrackDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object Playlists : Screen("playlists")
    object Favorites : Screen("favorites")
    object CreatePlaylist : Screen("create_playlist")
    object TrackDetails : Screen("track_details")
    object PlaylistDetails : Screen("playlist/{id}")
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
            val searchViewModel: SearchViewModel = koinViewModel()

            SearchScreen(
                viewModel = searchViewModel,
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

        composable(Screen.Playlists.route) {
            val viewModel: PlaylistsViewModel = koinViewModel()

            PlaylistsScreen(
                playlistsViewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onCreatePlaylistClick = { navController.navigate(Screen.CreatePlaylist.route) },
                navigateToPlaylist = { id -> navController.navigate("playlist/$id") }
            )
        }

        composable(
            route = Screen.PlaylistDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("id") ?: 0L

            val viewModel: PlaylistDetailsViewModel = koinViewModel(
                parameters = { parametersOf(playlistId) }
            )

            PlaylistDetailsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("track", track)
                    navController.navigate(Screen.TrackDetails.route)
                }
            )
        }

        composable(Screen.Favorites.route) {
            val viewModel: FavoritesViewModel = koinViewModel()

            FavoritesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("track", track)
                    navController.navigate(Screen.TrackDetails.route)
                }
            )
        }

        composable(Screen.CreatePlaylist.route) { backStackEntry ->
            val playlistsViewModel: PlaylistsViewModel = koinViewModel()

            CreatePlaylistScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { name, desc ->
                    playlistsViewModel.createNewPlaylist(name, desc)
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.TrackDetails.route) { backStackEntry ->
            val track = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Track>("track")


            val viewModel: TrackDetailsViewModel = koinViewModel(
                parameters = { parametersOf(track) }
            )

            TrackDetailsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}