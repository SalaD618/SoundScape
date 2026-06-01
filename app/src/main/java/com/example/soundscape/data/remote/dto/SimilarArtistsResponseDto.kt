package com.example.soundscape.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SimilarArtistsResponseDto(
    @SerializedName("similarartists")
    val similarArtists: SimilarArtistsDto
)

data class SimilarArtistsDto(
    @SerializedName("artist")
    val artist: List<ArtistDto>
)