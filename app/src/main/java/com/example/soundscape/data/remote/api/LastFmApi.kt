package com.example.soundscape.data.remote.api

import com.example.soundscape.data.remote.dto.ArtistSearchResponseDto
import com.example.soundscape.data.remote.dto.TopArtistsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.soundscape.data.remote.dto.ArtistInfoResponseDto

interface LastFmApi {

    @GET("2.0/")
    suspend fun getTopArtists(
        @Query("method") method: String = "chart.gettopartists",
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10
    ): TopArtistsResponseDto

    @GET("2.0/")
    suspend fun searchArtists(
        @Query("method") method: String = "artist.search",
        @Query("artist") artist: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10
    ): ArtistSearchResponseDto

    @GET("2.0/")
    suspend fun getArtistInfo(
        @Query("method") method: String = "artist.getinfo",
        @Query("artist") artist: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): ArtistInfoResponseDto
}