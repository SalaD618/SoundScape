package com.example.soundscape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundscape.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {

            repository.getFavoriteArtists()
                .collectLatest { artists ->

                    val topByPlaycount =
                        artists.maxByOrNull {
                            it.playcount.toLongOrNull() ?: 0L
                        }

                    val topByListeners =
                        artists.maxByOrNull {
                            it.listeners.toLongOrNull() ?: 0L
                        }

                    _uiState.value = StatsUiState(
                        totalFavorites = artists.size,

                        topArtist =
                            topByPlaycount?.name.orEmpty(),

                        topPlaycount =
                            topByPlaycount?.playcount.orEmpty(),

                        topListeners =
                            topByListeners?.listeners.orEmpty()
                    )
                }
        }
    }
}