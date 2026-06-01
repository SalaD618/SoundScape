package com.example.soundscape.viewmodel

import com.example.soundscape.domain.model.Artist

data class StatsUiState(
    val totalFavorites: Int = 0,
    val topArtist: String = "",
    val topPlaycount: String = "",
    val topListeners: String = "",
    val allFavorites: List<Artist> = emptyList(),
    val topByPlaycount: List<Artist> = emptyList(),
    val topByListeners: List<Artist> = emptyList(),
    val avgPlaycount: Long = 0L,
    val totalPlaycount: Long = 0L
)