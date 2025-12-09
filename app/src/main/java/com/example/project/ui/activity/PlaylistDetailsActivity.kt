package com.example.project.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project.R
import com.example.project.domain.Track
import com.example.project.ui.theme.SurfaceWhite
import com.example.project.ui.theme.TextPrimary
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.view_model.PlaylistDetailsViewModel

@Composable
fun PlaylistDetailsScreen(
    viewModel: PlaylistDetailsViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val playlist by viewModel.playlist.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(SurfaceWhite),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.cd_back),
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = playlist?.name ?: stringResource(R.string.playlist_details_title),
                    color = TextPrimary ,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (playlist == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Загрузка...", color = TextSecondary)
            }
            return
        }

        Text(
            text = playlist!!.description,
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val tracks = playlist!!.tracks

        if (tracks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Треков нет", color = TextSecondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(tracks) { track ->
                    PlaylistTrackItem(track) {
                        onTrackClick(track)
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistTrackItem(track: Track, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.image,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(track.trackName, fontSize = 16.sp, color = TextPrimary)
            Text("${track.artistName} • ${track.trackTime}", color = TextSecondary)
        }
    }
}
