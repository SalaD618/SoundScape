package com.example.soundscape.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soundscape.viewmodel.DetailsViewModel

@Composable
fun DetailsScreen(
    artistName: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(artistName) {
        viewModel.loadArtistDetails(artistName)
    }

    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Loading artist details...",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        uiState.errorMessage != null -> {
            Text(
                text = uiState.errorMessage ?: "Unknown error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(24.dp)
            )
        }

        uiState.artist != null -> {
            val artist = uiState.artist!!

            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                        .padding(bottom = 80.dp)
                ) {
                    Text(
                        text = artist.name,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text(
                        text = "${artist.listeners} listeners",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "${artist.playcount} plays",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (artist.tags.isNotEmpty()) {
                        FlowRow(
                            modifier = Modifier.padding(top = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            artist.tags.take(5).forEach { tag ->
                                AssistChip(
                                    onClick = {},
                                    label = { Text(text = tag) }
                                )
                            }
                        }
                    }

                    Text(
                        text = "Biography",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 24.dp)
                    )

                    Text(
                        text = artist.summary.ifBlank { "No biography available." },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                FloatingActionButton(
                    onClick = { viewModel.toggleFavorite() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (uiState.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (uiState.isFavorite) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
