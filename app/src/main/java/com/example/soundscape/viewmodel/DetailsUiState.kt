package com.example.soundscape.viewmodel

import com.example.soundscape.domain.model.ArtistDetails
import com.example.soundscape.domain.model.Artist

data class DetailsUiState(
    val isLoading: Boolean = false,
    val artist: ArtistDetails? = null,
    val similarArtists: List<Artist> = emptyList(),
    val errorMessage: String? = null,
    val isFavorite: Boolean = false
)
