package com.example.soundscape.data.repository

import com.example.soundscape.data.local.dao.FavoriteArtistDao
import com.example.soundscape.data.local.entity.FavoriteArtistEntity
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
//    private val wikimediaApi: WikimediaApi,
    private val favoriteArtistDao: FavoriteArtistDao
) {
    // ─── Remote ───────────────────────────────────────────────

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
        ).similarArtists.artist.map {
            it.toArtist()
        }
    }

    fun isArtistFavorite(artistName: String): Flow<Boolean> {
        return favoriteArtistDao.isFavorite(artistName)
    }

//    suspend fun getArtistImage(artistName: String): String {
//        return try {
//            wikimediaApi.getArtistImage(titles = artistName)
//                .query?.pages?.values
//                ?.firstOrNull()
//                ?.thumbnail?.source
//                .orEmpty()
//        } catch (e: Exception) {
//            ""
//        }
//    }
}
