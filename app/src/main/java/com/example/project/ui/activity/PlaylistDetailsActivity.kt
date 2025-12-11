package com.example.project.ui.activity

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project.R
import com.example.project.domain.Track
import com.example.project.ui.theme.SurfaceWhite
import com.example.project.ui.theme.TextPrimary
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.view_model.PlaylistDetailsViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PlaylistDetailsScreen(
    viewModel: PlaylistDetailsViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val playlist by viewModel.playlist.collectAsState()

    if (playlist == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.loading))
        }
        return
    }

    val tracks = playlist!!.tracks
    val totalMinutes = viewModel.getTotalDurationMinutes(tracks)
    val countText = viewModel.getTracksCountText(tracks.size)

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val coverHeight = screenHeight * 0.40f

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
                    text = playlist!!.name,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .height(coverHeight)
                .clip(RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = playlist!!.coverImageUri,
                contentDescription = playlist!!.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            Text(
                text = playlist!!.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            if (playlist!!.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = playlist!!.description,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "$totalMinutes мин • $countText",
                color = TextPrimary,
                fontSize = 15.sp
            )
        }

        if (tracks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_tracks), color = TextSecondary)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tracks) { track ->
                    PlaylistTrackItem(
                        track = track,
                        onClick = { onTrackClick(track) },
                        onLongClick = { viewModel.removeTrack(track) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistTrackItem(
    track: Track,
    onClick: (Track) -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 7.dp)
            .combinedClickable(
                onClick = { onClick(track) },
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = track.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_music),
                    error = painterResource(id = R.drawable.ic_music)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = track.trackName,
                        fontSize = 16.sp,
                        color = TextPrimary,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = buildAnnotatedString {
                            append(track.artistName)
                            append(" • ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(track.trackTime)
                            }
                        },
                        fontSize = 14.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }
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
