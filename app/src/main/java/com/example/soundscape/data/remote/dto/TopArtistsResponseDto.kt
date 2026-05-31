package com.example.soundscape.data.remote.dto


import com.google.gson.annotations.SerializedName

data class TopArtistsResponseDto(
    @SerializedName("artists")
    val artists: ArtistsDto
)

data class ArtistsDto(
    @SerializedName("artist")
    val artist: List<ArtistDto>
)

//data class ArtistDto(
//    @SerializedName("name")
//    val name: String,
//
//    @SerializedName("listeners")
//    val listeners: String,
//
//    @SerializedName("playcount")
//    val playcount: String,
//
//    @SerializedName("url")
//    val url: String,
//
//    @SerializedName("image")
//    val image: List<ArtistImageDto>
//)
data class ArtistDto(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("listeners")
    val listeners: String? = null,

    @SerializedName("playcount")
    val playcount: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("image")
    val image: List<ArtistImageDto>? = emptyList()
)

data class ArtistImageDto(
    @SerializedName("#text")
    val url: String? = null,

    @SerializedName("size")
    val size: String? = null
)