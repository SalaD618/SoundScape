package com.example.soundscape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundscape.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = repository
        .getFavoriteArtists()
        .map { artists -> FavoritesUiState(favorites = artists) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState()
        )

    fun removeFavorite(artistName: String) {
        viewModelScope.launch {
            repository.removeFavorite(artistName)
        }
    }
}
