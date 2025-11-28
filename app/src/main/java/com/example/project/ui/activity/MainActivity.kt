package com.example.project.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project.R
import com.example.project.domain.Track
import com.example.project.ui.navigation.Screen
import com.example.project.ui.theme.PrimaryBlue
import com.example.project.ui.theme.ProjectTheme
import com.example.project.ui.theme.TextPrimary
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.theme.White
import com.example.project.ui.view_model.TrackDetailsViewModel
import com.example.project.ui.view_model.TrackDetailsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PrimaryBlue
                ) {
                    PlaylistHost()
                }
            }
        }
    }
}

@Composable
fun PlaylistMakerScreen(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPlaylistsClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(PrimaryBlue),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                MenuButton(
                    text = stringResource(R.string.search),
                    iconResId = android.R.drawable.ic_menu_search,
                    onClick = onSearchClick
                )

                MenuButton(
                    text = stringResource(R.string.playlists),
                    iconResId = R.drawable.ic_playlists,
                    onClick = onPlaylistsClick
                )

                MenuButton(
                    text = stringResource(R.string.favorites),
                    iconResId = R.drawable.ic_favorite_outline,
                    onClick = onFavoritesClick
                )

                MenuButton(
                    text = stringResource(R.string.settings),
                    iconResId = R.drawable.ic_settings_gear,
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuButton(
    text: String,
    iconResId: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = TextPrimary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = text,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = TextSecondary
            )
        }
    }
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

        composable(Screen.Playlists.route) {
            PlaylistsScreen(
                onBackClick = { navController.popBackStack() },
                onCreatePlaylistClick = { navController.navigate(Screen.CreatePlaylist.route) }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.CreatePlaylist.route) {
            CreatePlaylistScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { _, _ -> navController.popBackStack() }
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
                factory = TrackDetailsViewModelFactory(track)
            )

            TrackDetailsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PlaylistMakerScreenPreview() {
    ProjectTheme {
        PlaylistMakerScreen(
            onSearchClick = {},
            onSettingsClick = {},
            onPlaylistsClick = {},
            onFavoritesClick = {}
        )
    }
}
