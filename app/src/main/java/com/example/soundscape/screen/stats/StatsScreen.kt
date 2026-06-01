package com.example.soundscape.screen.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soundscape.viewmodel.StatsViewModel

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Your Statistics",
            style = MaterialTheme.typography.headlineMedium
        )

        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Favorite Artists")
                Text(
                    text = uiState.totalFavorites.toString(),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Most Popular Favorite")

                Text(
                    text = uiState.topArtist.ifBlank { "-" },
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "${uiState.topPlaycount} plays"
                )
            }
        }

        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text("Highest Listener Count")

                Text(
                    text = uiState.topListeners.ifBlank { "-" },
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}