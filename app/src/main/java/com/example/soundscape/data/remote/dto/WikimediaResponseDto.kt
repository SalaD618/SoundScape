package com.example.soundscape.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WikimediaResponseDto(
    @SerializedName("query")
    val query: WikimediaQueryDto? = null
)

data class WikimediaQueryDto(
    @SerializedName("pages")
    val pages: Map<String, WikimediaPageDto>? = null
)

data class WikimediaPageDto(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: WikimediaThumbnailDto? = null
)

data class WikimediaThumbnailDto(
    @SerializedName("source")
    val source: String? = null
)