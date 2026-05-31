package com.example.soundscape.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundscape.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MusicRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)

        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.value = SearchUiState()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            searchArtists(query)
        }
    }

    private suspend fun searchArtists(query: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        try {
            val apiKeyResourceId = context.resources.getIdentifier(
                "lastfm_api_key",
                "string",
                context.packageName
            )

            if (apiKeyResourceId == 0) {
                throw IllegalStateException("LASTFM_API_KEY is missing from local.properties")
            }

            val artists = repository.searchArtists(
                query = query,
                apiKey = context.getString(apiKeyResourceId)
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                artists = artists
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = e.message ?: "Something went wrong"
            )
        }
    }
}