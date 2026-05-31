package com.example.soundscape.data.repository

import com.example.soundscape.data.remote.api.LastFmApi
import com.example.soundscape.data.remote.dto.toArtist
import com.example.soundscape.domain.model.Artist
import javax.inject.Inject
import com.example.soundscape.data.remote.dto.toArtistDetails
import com.example.soundscape.domain.model.ArtistDetails

class MusicRepository @Inject constructor(
    private val api: LastFmApi
) {
    suspend fun getTopArtists(apiKey: String): List<Artist> {
        return api.getTopArtists(
            apiKey = apiKey,
            limit = 10
        ).artists.artist.map { it.toArtist() }
    }

    suspend fun searchArtists(
        query: String,
        apiKey: String
    ): List<Artist> {
        if (query.isBlank()) return emptyList()

        return api.searchArtists(
            artist = query,
            apiKey = apiKey,
            limit = 10
        ).results.artistMatches.artist.map { it.toArtist() }
    }

    suspend fun getArtistDetails(
        artistName: String,
        apiKey: String
    ): ArtistDetails {
        return api.getArtistInfo(
            artist = artistName,
            apiKey = apiKey
        ).artist.toArtistDetails()
    }
}