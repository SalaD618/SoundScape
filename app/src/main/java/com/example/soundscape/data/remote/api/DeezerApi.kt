package com.example.soundscape.data.remote.api

import com.example.soundscape.data.remote.dto.DeezerArtistSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerApi {
    @GET("search/artist")
    suspend fun searchArtist(
        @Query("q") artistName: String
    ): DeezerArtistSearchResponseDto
}
