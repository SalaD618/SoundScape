package com.example.soundscape.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeezerArtistSearchResponseDto(
    @SerializedName("data")
    val data: List<DeezerArtistDto> = emptyList()
)

data class DeezerArtistDto(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("picture_medium")
    val pictureMedium: String? = null,
    @SerializedName("picture_big")
    val pictureBig: String? = null,
    @SerializedName("picture_xl")
    val pictureXl: String? = null
)
