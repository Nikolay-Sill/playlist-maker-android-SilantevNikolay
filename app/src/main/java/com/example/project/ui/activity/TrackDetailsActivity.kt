package com.example.project.ui.activity

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project.R
import com.example.project.domain.Track
import com.example.project.ui.theme.SurfaceWhite
import com.example.project.ui.view_model.TrackDetailsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    viewModel: TrackDetailsViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is TrackDetailsViewModel.State.Content -> {
            val track = (state as TrackDetailsViewModel.State.Content).track
            TrackDetailsScreenContent(track, onBackClick)
        }

        is TrackDetailsViewModel.State.Error -> TODO()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackDetailsScreenContent(
    track: Track,
    onBackClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.add_to_playlist),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Нет плейлистов",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.48f)
        ) {
            AsyncImage(
                model = track.image,
                placeholder = painterResource(id = R.drawable.ic_music),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(16.dp)
                    .size(27.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back),
                    tint = Color.Black
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = track.trackName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = track.artistName,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    onClick = {
                        isSheetOpen = true
                        scope.launch { sheetState.show() }
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_playlist),
                            contentDescription = stringResource(R.string.add_to_playlist),
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite_outline),
                            contentDescription = stringResource(R.string.add_to_favorites),
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.duration),
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )

                Text(
                    text = track.trackTime,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TrackDetailsScreenError(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Ошибка: трек не найден")
        }
    }
}
