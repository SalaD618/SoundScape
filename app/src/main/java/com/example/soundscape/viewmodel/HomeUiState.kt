package com.example.soundscape.viewmodel

import com.example.soundscape.domain.model.Artist

data class HomeUiState(
    val isLoading: Boolean = false,
    val artists: List<Artist> = emptyList(),
    val errorMessage: String? = null
)