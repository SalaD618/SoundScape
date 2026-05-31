package com.example.soundscape.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArtistInfoResponseDto(
    @SerializedName("artist")
    val artist: ArtistInfoDto
)
data class ArtistInfoDto(
@SerializedName("name")
val name: String? = null,
@SerializedName("stats")
val stats: ArtistStatsDto? = null,
@SerializedName("url")
val url: String? = null,
@SerializedName("bio")
val bio: ArtistBioDto? = null,
@SerializedName("tags")
val tags: ArtistTagsDto? = null,
@SerializedName("image")
val image: List<ArtistImageDto>? = emptyList()
)

data class ArtistBioDto(
    @SerializedName("summary")
    val summary: String? = null,

    @SerializedName("content")
    val content: String? = null
)

data class ArtistTagsDto(
    @SerializedName("tag")
    val tag: List<ArtistTagDto>? = emptyList()
)

data class ArtistTagDto(
    @SerializedName("name")
    val name: String? = null
)

data class ArtistStatsDto(
    @SerializedName("listeners")
    val listeners: String? = null,
    @SerializedName("playcount")
    val playcount: String? = null
)