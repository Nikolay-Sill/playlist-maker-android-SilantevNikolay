package com.example.project.ui.activity

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.R
import com.example.project.ui.theme.DarkGrey
import com.example.project.ui.theme.ProjectTheme
import com.example.project.ui.theme.SurfaceWhite
import com.example.project.ui.theme.TextPrimary

@Composable
fun PlaylistsScreen(
    onBackClick: () -> Unit,
    onCreatePlaylistClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceWhite)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
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
                        text = stringResource(R.string.playlists),
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.playlists_empty),
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            }
        }

        FloatingActionButton(
            onClick = onCreatePlaylistClick,
            modifier = Modifier
                .padding(bottom = 32.dp, end = 16.dp)
                .align(Alignment.BottomEnd)
                .size(56.dp)
                .clip(CircleShape),
            containerColor = DarkGrey,
            contentColor = SurfaceWhite
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.cd_create_playlist),
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PlaylistsScreenPreview() {
    ProjectTheme {
        PlaylistsScreen(
            onBackClick = {},
            onCreatePlaylistClick = {}
        )
    }
}