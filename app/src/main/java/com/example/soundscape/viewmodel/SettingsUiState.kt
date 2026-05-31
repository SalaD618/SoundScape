package com.example.soundscape.viewmodel

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val defaultGenre: String = "All",
    val language: String = "English"
)
