package com.example.soundscape.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundscape.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MusicRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTopArtists()
    }

    fun loadTopArtists() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)

            try {
                val apiKeyResourceId = context.resources.getIdentifier(
                    "lastfm_api_key",
                    "string",
                    context.packageName
                )

                if (apiKeyResourceId == 0) {
                    throw IllegalStateException("LASTFM_API_KEY is missing from local.properties")
                }

                val artists = repository.getTopArtists(
                    apiKey = context.getString(apiKeyResourceId)
                )

                _uiState.value = HomeUiState(
                    isLoading = false,
                    artists = artists
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Something went wrong"
                )
            }
        }
    }
}