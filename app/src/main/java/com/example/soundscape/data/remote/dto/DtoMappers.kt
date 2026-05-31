package com.example.soundscape.data.remote.dto

import com.example.soundscape.domain.model.Artist
import com.example.soundscape.domain.model.ArtistDetails

fun ArtistDto.toArtist(): Artist {
    val bestImage = image
        ?.lastOrNull { !it.url.isNullOrBlank() }
        ?.url
        .orEmpty()

    return Artist(
        name = name.orEmpty(),
        listeners = listeners ?: "0",
        playcount = playcount ?: "0",
        imageUrl = bestImage,
        url = url.orEmpty()
    )
}
fun ArtistInfoDto.toArtistDetails(): ArtistDetails {
    val bestImage = image
        ?.lastOrNull { !it.url.isNullOrBlank() }
        ?.url
        .orEmpty()

    return ArtistDetails(
        name = name.orEmpty(),
        listeners = listeners ?: "0",
        playcount = playcount ?: "0",
        imageUrl = bestImage,
        summary = bio?.summary.orEmpty(),
        tags = tags?.tag
            ?.mapNotNull { it.name }
            ?.filter { it.isNotBlank() }
            .orEmpty(),
        url = url.orEmpty()
    )
}