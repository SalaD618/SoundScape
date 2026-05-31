package com.example.soundscape.data.local.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.soundscape.data.local.dao.FavoriteArtistDao
import com.example.soundscape.data.local.entity.FavoriteArtistEntity

@Database(
    entities = [FavoriteArtistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SoundScapeDatabase : RoomDatabase() {
    abstract fun favoriteArtistDao(): FavoriteArtistDao
}
