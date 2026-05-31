package com.example.soundscape.data.remote.api

import com.example.soundscape.data.remote.dto.WikimediaResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WikimediaApi {

    @GET("w/api.php")
    suspend fun getArtistImage(
        @Query("action") action: String = "query",
        @Query("titles") titles: String,
        @Query("prop") prop: String = "pageimages",
        @Query("pithumbsize") size: Int = 500,
        @Query("format") format: String = "json"
    ): WikimediaResponseDto
}