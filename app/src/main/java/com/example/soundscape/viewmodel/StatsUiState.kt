package com.example.soundscape.viewmodel

data class StatsUiState(
    val totalFavorites: Int = 0,
    val topArtist: String = "",
    val topPlaycount: String = "",
    val topListeners: String = ""
)