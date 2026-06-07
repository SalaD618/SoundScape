package com.example.soundscape.data.repository

import com.example.soundscape.data.local.dao.FavoriteArtistDao
import com.example.soundscape.data.local.entity.FavoriteArtistEntity
import com.example.soundscape.data.remote.api.DeezerApi
import com.example.soundscape.data.remote.api.LastFmApi
import com.example.soundscape.data.remote.dto.toArtist
import com.example.soundscape.data.remote.dto.toArtistDetails
import com.example.soundscape.domain.model.Artist
import com.example.soundscape.domain.model.ArtistDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val api: LastFmApi,
    private val deezerApi: DeezerApi,
    private val favoriteArtistDao: FavoriteArtistDao
) {
    // ─── Remote ───────────────────────────────────────────────

    suspend fun getTopArtists(apiKey: String): List<Artist> {
        return api.getTopArtists(
            apiKey = apiKey,
            limit = 10
        ).artists.artist.map { artistDto ->
            artistDto.toArtist().withDeezerImage()
        }
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
        ).results.artistMatches.artist.map { artistDto ->
            artistDto.toArtist().withDeezerImage()
        }
    }

    suspend fun getArtistDetails(
        artistName: String,
        apiKey: String
    ): ArtistDetails {
        val details = api.getArtistInfo(
            artist = artistName,
            apiKey = apiKey
        ).artist.toArtistDetails()

        return details.copy(
            imageUrl = details.imageUrl.ifBlank {
                getArtistImageUrl(details.name)
            }
        )
    }

    // ─── Favorites (Room) ─────────────────────────────────────

    fun getFavoriteArtists(): Flow<List<Artist>> {
        return favoriteArtistDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                Artist(
                    name = entity.name,
                    listeners = entity.listeners,
                    playcount = entity.playcount,
                    imageUrl = entity.imageUrl,
                    url = entity.url
                )
            }
        }
    }

    suspend fun addFavorite(artist: Artist) {
        favoriteArtistDao.insertFavorite(
            FavoriteArtistEntity(
                name = artist.name,
                listeners = artist.listeners,
                playcount = artist.playcount,
                imageUrl = artist.imageUrl,
                url = artist.url
            )
        )
    }

    suspend fun removeFavorite(artistName: String) {
        favoriteArtistDao.deleteFavoriteByName(artistName)
    }

    suspend fun getSimilarArtists(
        artistName: String,
        apiKey: String
    ): List<Artist> {

        return api.getSimilarArtists(
            artist = artistName,
            apiKey = apiKey
        ).similarArtists.artist.map { artistDto ->
            artistDto.toArtist().withDeezerImage()
        }
    }

    fun isArtistFavorite(artistName: String): Flow<Boolean> {
        return favoriteArtistDao.isFavorite(artistName)
    }

    private suspend fun Artist.withDeezerImage(): Artist {
        if (imageUrl.isNotBlank()) return this

        return copy(
            imageUrl = getArtistImageUrl(name)
        )
    }

    private suspend fun getArtistImageUrl(artistName: String): String {
        if (artistName.isBlank()) return ""

        return try {
            val response = deezerApi.searchArtist(artistName)
            val artist = response.data.firstOrNull {
                it.name.equals(artistName, ignoreCase = true)
            } ?: response.data.firstOrNull()

            artist?.pictureXl
                ?: artist?.pictureBig
                ?: artist?.pictureMedium
                ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}
