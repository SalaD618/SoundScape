package com.example.soundscape.data.remote.dto

import com.example.soundscape.domain.model.Artist
import com.example.soundscape.domain.model.ArtistDetails

fun ArtistDto.toArtist(): Artist {

    val imageUrl = image
        ?.lastOrNull { !it.url.isNullOrBlank() }
        ?.url
        .orEmpty()

    val finalImage = if (
        imageUrl.contains("2a96cbd8b46e442fc41c2b86b821562f")
    ) {
        ""
    } else {
        imageUrl
    }

    return Artist(
        name = name.orEmpty(),
        listeners = listeners ?: "0",
        playcount = playcount ?: "0",
        imageUrl = finalImage,
        url = url.orEmpty()
    )
}
fun ArtistInfoDto.toArtistDetails(): ArtistDetails {
    val imageUrl = image
        ?.lastOrNull { !it.url.isNullOrBlank() }
        ?.url
        .orEmpty()

    val finalImage = if (
        imageUrl.contains("2a96cbd8b46e442fc41c2b86b821562f")
    ) {
        ""
    } else {
        imageUrl
    }

    return ArtistDetails(
        name = name.orEmpty(),
        listeners = stats?.listeners ?: "0",
        playcount = stats?.playcount ?: "0",
        imageUrl = finalImage,
        summary = bio?.summary.orEmpty(),
        tags = tags?.tag
            ?.mapNotNull { it.name }
            ?.filter { it.isNotBlank() }
            .orEmpty(),
        url = url.orEmpty()
    )
}
