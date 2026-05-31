package com.example.soundscape.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_artists")
data class FavoriteArtistEntity(
    @PrimaryKey
    val name: String,
    val listeners: String,
    val playcount: String,
    val imageUrl: String,
    val url: String,
    val savedAt: Long = System.currentTimeMillis()
)
