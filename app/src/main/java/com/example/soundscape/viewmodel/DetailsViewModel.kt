package com.example.soundscape.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundscape.data.repository.MusicRepository
import com.example.soundscape.domain.model.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: MusicRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun loadArtistDetails(artistName: String) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState(isLoading = true)

            try {
                val apiKeyResourceId = context.resources.getIdentifier(
                    "lastfm_api_key",
                    "string",
                    context.packageName
                )

                if (apiKeyResourceId == 0) {
                    throw IllegalStateException("LASTFM_API_KEY is missing from local.properties")
                }

                val artist = repository.getArtistDetails(
                    artistName = artistName,
                    apiKey = context.getString(apiKeyResourceId)
                )

                _uiState.value = DetailsUiState(
                    isLoading = false,
                    artist = artist
                )

                // Observe favorite status reactively
                repository.isArtistFavorite(artistName).collect { isFav ->
                    _uiState.value = _uiState.value.copy(isFavorite = isFav)
                }
            } catch (e: Exception) {
                _uiState.value = DetailsUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun toggleFavorite() {
        val current = _uiState.value.artist ?: return
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                repository.removeFavorite(current.name)
            } else {
                repository.addFavorite(
                    Artist(
                        name = current.name,
                        listeners = current.listeners,
                        playcount = current.playcount,
                        imageUrl = current.imageUrl,
                        url = current.url
                    )
                )
            }
        }
    }
}
