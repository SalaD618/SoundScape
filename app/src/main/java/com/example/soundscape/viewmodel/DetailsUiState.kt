package com.example.soundscape.viewmodel

import com.example.soundscape.domain.model.ArtistDetails

data class DetailsUiState(
    val isLoading: Boolean = false,
    val artist: ArtistDetails? = null,
    val errorMessage: String? = null
)