package com.example.soundscape.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArtistSearchResponseDto(
    @SerializedName("results")
    val results: SearchResultsDto
)

data class SearchResultsDto(
    @SerializedName("artistmatches")
    val artistMatches: ArtistMatchesDto
)

data class ArtistMatchesDto(
    @SerializedName("artist")
    val artist: List<ArtistDto>
)