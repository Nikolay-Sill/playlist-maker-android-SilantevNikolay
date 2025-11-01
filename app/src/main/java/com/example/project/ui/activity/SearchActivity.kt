package com.example.project.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.R
import com.example.project.ui.view_model.SearchViewModel
import com.example.project.ui.TrackListItem
import com.example.project.ui.theme.ProjectTheme

@Composable
fun SearchScreen(
    onBackClick: () -> Unit
) {
    LocalContext.current
    val searchQuery = remember { mutableStateOf("") }
    val viewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
    val screenState by viewModel.searchScreenState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White),
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
                        contentDescription = "Назад",
                        tint = Color(0xFF1A1B22)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Поиск",
                    color = Color(0xFF1A1B22),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { newValue ->
                    searchQuery.value = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp),
                placeholder = {
                    Text(
                        text = "Поиск",
                        color = Color(0xFFAEAFB4),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            if (searchQuery.value.isNotEmpty()) {
                                viewModel.search(searchQuery.value)
                            }
                        },
                        modifier = Modifier.size(18.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Поиск",
                            tint = Color(0xFFAEAFB4)
                        )
                    }
                },
                trailingIcon = {
                    if (searchQuery.value.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchQuery.value = ""
                            },
                            modifier = Modifier.size(18.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Очистить",
                                tint = Color(0xFFAEAFB4)
                            )
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE6E8EB),
                    unfocusedBorderColor = Color(0xFFE6E8EB),
                    focusedTextColor = Color(0xFF1A1B22),
                    unfocusedTextColor = Color(0xFF1A1B22),
                    cursorColor = Color(0xFF1A1B22),
                    focusedContainerColor = Color(0xFFE6E8EB),
                    unfocusedContainerColor = Color(0xFFE6E8EB)
                ),
                shape = RoundedCornerShape(6.dp)
            )
        }

        when (screenState) {
            is com.example.project.domain.SearchState.Initial -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Введите строку для поиска",
                        color = Color(0xFFAEAFB4)
                    )
                }
            }

            is com.example.project.domain.SearchState.Searching -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is com.example.project.domain.SearchState.Success -> {
                val tracks = (screenState as com.example.project.domain.SearchState.Success).list
                if (tracks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ничего не найдено",
                            color = Color(0xFFAEAFB4)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(tracks) { track ->
                            TrackListItem(track = track)
                            Divider(
                                color = Color(0xFFE6E8EB),
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }

            is com.example.project.domain.SearchState.Fail -> {
                val error = (screenState as com.example.project.domain.SearchState.Fail).error
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ошибка: $error",
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SearchScreenPreview() {
    ProjectTheme {
        SearchScreen(
            onBackClick = {}
        )
    }
}