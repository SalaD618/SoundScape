package com.example.soundscape.viewmodel

import com.example.soundscape.domain.model.Artist

data class FavoritesUiState(
    val favorites: List<Artist> = emptyList()
)
