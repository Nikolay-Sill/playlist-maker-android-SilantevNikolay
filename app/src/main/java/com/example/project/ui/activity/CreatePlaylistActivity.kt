package com.example.project.ui.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.project.ui.theme.BackgroundGray
import com.example.project.ui.theme.PrimaryBlue
import com.example.project.ui.theme.ProjectTheme
import com.example.project.ui.theme.SurfaceWhite
import com.example.project.ui.theme.TextPrimary
import com.example.project.ui.theme.TextSecondary
import com.example.project.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistScreen(
    onBackClick: () -> Unit,
    onSaveClick: (String, String) -> Unit
) {
    val playlistName = remember { mutableStateOf("") }
    val playlistDescription = remember { mutableStateOf("") }

    val isButtonEnabled = playlistName.value.isNotBlank()
    val isNameFieldFocused = remember { mutableStateOf(false) }

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
                    text = stringResource(R.string.create_playlist_title),
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(White)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add_photo),
                    contentDescription = stringResource(R.string.cd_add_photo),
                    modifier = Modifier.size(100.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = playlistName.value,
                    onValueChange = { playlistName.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    label = {
                        Text(
                            text = stringResource(R.string.playlist_name_required),
                            color = if (isNameFieldFocused.value || playlistName.value.isNotEmpty()) PrimaryBlue else TextSecondary,
                            fontSize = 16.sp
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = if (playlistName.value.isNotEmpty()) PrimaryBlue else BackgroundGray,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = PrimaryBlue,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedLabelColor = PrimaryBlue,
                        unfocusedLabelColor = if (playlistName.value.isNotEmpty()) PrimaryBlue else TextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = playlistDescription.value,
                    onValueChange = { playlistDescription.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    label = {
                        Text(
                            text = stringResource(R.string.playlist_description_hint),
                            color = if (playlistDescription.value.isNotEmpty()) PrimaryBlue else TextSecondary,
                            fontSize = 16.sp
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (playlistDescription.value.isNotEmpty()) PrimaryBlue else BackgroundGray,
                        unfocusedBorderColor = if (playlistDescription.value.isNotEmpty()) PrimaryBlue else BackgroundGray,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = PrimaryBlue,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedLabelColor = PrimaryBlue,
                        unfocusedLabelColor = if (playlistDescription.value.isNotEmpty()) PrimaryBlue else TextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            color = if (isButtonEnabled) PrimaryBlue else BackgroundGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (isButtonEnabled) {
                                onSaveClick(playlistName.value, playlistDescription.value)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.create),
                        color = if (isButtonEnabled) SurfaceWhite else TextSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun CreatePlaylistScreenPreview() {
    ProjectTheme {
        CreatePlaylistScreen(
            onBackClick = {},
            onSaveClick = { _, _ -> }
        )
    }
}