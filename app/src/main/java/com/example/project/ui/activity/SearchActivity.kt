package com.example.project.ui.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.project.R
import com.example.project.domain.Track
import com.example.project.domain.Word
import com.example.project.ui.theme.*
import com.example.project.ui.view_model.SearchState
import com.example.project.ui.view_model.SearchViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val screenState by viewModel.searchScreenState.collectAsState()

    var historyList by remember { mutableStateOf<List<Word>>(emptyList()) }
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(screenState) {
        if (screenState is SearchState.Success) {
            focusManager.clearFocus()
            historyList = viewModel.getHistoryList()
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
                .height(56.dp),
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.search_title),
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BackgroundGray)
            ) {

                SearchBasicField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        viewModel.onQueryChange(newText)
                    },
                    placeholderText = stringResource(R.string.search_songs_placeholder),
                    focusRequester = focusRequester,
                    onFocusChanged = { focused ->
                        isFocused = focused
                        if (focused && text.isEmpty()) {
                            coroutineScope.launch {
                                historyList = viewModel.getHistoryList()
                            }
                        }
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                if (text.isNotEmpty()) viewModel.search(text)
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = TextSecondary
                            )
                        }
                    },
                    trailingIcon = if (text.isNotEmpty()) {
                        {
                            IconButton(
                                onClick = {
                                    text = ""
                                    viewModel.clearSearch()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                        }
                    } else null
                )

                if (isFocused && text.isEmpty() && historyList.isNotEmpty()) {
                    HistoryRequests(
                        historyList = historyList,
                        onClick = { word ->
                            text = word
                            viewModel.search(word)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        when (screenState) {

            is SearchState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (text.isEmpty())
                            stringResource(R.string.enter_search_query)
                        else
                            stringResource(R.string.press_search_button),
                        color = TextSecondary
                    )
                }
            }

            is SearchState.Searching -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SearchState.Success -> {
                val tracks = (screenState as SearchState.Success).list

                if (tracks.isEmpty()) {
                    PlaceholderNoResults()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(tracks) { track ->
                            TrackListItem(
                                track = track,
                                onClick = { selectedTrack ->
                                    onTrackClick(selectedTrack)
                                }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 1.dp,
                                color = BackgroundGray
                            )
                        }
                    }
                }
            }

            is SearchState.Fail -> {
                PlaceholderError(
                    onRetry = {
                        if (text.isNotEmpty()) {
                            viewModel.search(text)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SearchBasicField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textFontSize: Int = 18,
) {
    val shape = RoundedCornerShape(6.dp)

    Box(
        modifier = modifier
            .height(49.dp)
            .clip(shape)
            .background(BackgroundGray)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(modifier = Modifier.weight(1f)) {

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = textFontSize.sp,
                        color = TextPrimary,
                        lineHeight = textFontSize.sp
                    ),
                    cursorBrush = SolidColor(PrimaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { state ->
                            onFocusChanged(state.isFocused)
                        },
                    decorationBox = { inner ->
                        if (value.isEmpty()) {
                            Text(
                                text = placeholderText,
                                fontSize = 16.sp,
                                color = TextSecondary
                            )
                        }
                        inner()
                    }
                )
            }

            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}

@Composable
fun TrackListItem(
    track: Track,
    onClick: (Track) -> Unit
) {
    Card(
        onClick = { onClick(track) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
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

@Composable
fun HistoryRequests(
    historyList: List<Word>,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = TextSecondary.copy(alpha = 0.2f)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            items(historyList) { historyItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(historyItem.word) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_history),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Text(
                        text = historyItem.word,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceholderNoResults() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.no_tracks),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.nothing_found),
            color = TextPrimary,
            fontSize = 16.sp
        )
    }
}

@Composable
fun PlaceholderError(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.without_internet),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.placeholder_error),
            color = TextPrimary,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.check_the_connection),
            color = TextPrimary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(PrimaryBlue)
                .clickable { onRetry() }
                .padding(horizontal = 28.dp, vertical = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.retry),
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SearchScreenPreview() {
    ProjectTheme {
        SearchScreen(
            viewModel = viewModel(),
            onBackClick = {},
            onTrackClick = {}
        )
    }
}
