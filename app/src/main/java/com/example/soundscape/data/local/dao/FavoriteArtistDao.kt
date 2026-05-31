package com.example.soundscape.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.soundscape.data.local.entity.FavoriteArtistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteArtistDao {

    @Query("SELECT * FROM favorite_artists ORDER BY savedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteArtistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(artist: FavoriteArtistEntity)

    @Query("DELETE FROM favorite_artists WHERE name = :artistName")
    suspend fun deleteFavoriteByName(artistName: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_artists WHERE name = :artistName)")
    fun isFavorite(artistName: String): Flow<Boolean>
}
