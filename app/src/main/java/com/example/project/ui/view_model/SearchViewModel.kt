package com.example.project.ui.view_model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project.R
import com.example.project.creator.Storage
import com.example.project.data.network.RetrofitNetworkClient
import com.example.project.data.network.SearchHistoryRepositoryImpl
import com.example.project.data.network.TracksRepositoryImpl
import com.example.project.domain.Track
import com.example.project.domain.TracksRepository
import com.example.project.domain.Word
import com.example.project.ui.theme.SurfaceWhite
import com.example.project.ui.theme.TextPrimary
import com.example.project.ui.theme.TextSecondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {
    private val searchHistoryRepository = SearchHistoryRepositoryImpl(scope = viewModelScope)

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    fun search(whatSearch: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.update { SearchState.Searching }

                searchHistoryRepository.addToHistory(Word(word = whatSearch))
                val list = tracksRepository.searchTracks(expression = whatSearch)
                _searchScreenState.update { SearchState.Success(list = list) }

            } catch (e: IOException) {
                _searchScreenState.update { SearchState.Fail(e.message.toString()) }
            }
        }
    }

    fun clearSearch() {
        _searchScreenState.update { SearchState.Initial }
    }

    suspend fun getHistoryList() = searchHistoryRepository.getHistory()

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.getTracksRepository()) as T
                }
            }
    }
}

@Composable
fun TrackListItem(
    track: Track,
    onClick: (Track) -> Unit = {}
) {
    Card(
        onClick = { onClick(track) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
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
                Icon(
                    painter = painterResource(id = R.drawable.ic_music),
                    contentDescription = "Трек ${track.trackName}",
                    modifier = Modifier.size(40.dp),
                    tint = TextPrimary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = track.trackName,
                        fontWeight = FontWeight.Normal,
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
                contentDescription = stringResource(R.string.cd_navigate),
                modifier = Modifier.size(24.dp),
                tint = TextSecondary
            )
        }
    }
}

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val list: List<Track>) : SearchState()
    data class Fail(val error: String) : SearchState()
}

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(Storage()))
    }
}