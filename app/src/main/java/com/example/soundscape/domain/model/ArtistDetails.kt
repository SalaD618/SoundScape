package com.example.soundscape.domain.model

data class ArtistDetails(
    val name: String,
    val listeners: String,
    val playcount: String,
    val imageUrl: String,
    val summary: String,
    val tags: List<String>,
    val url: String
)