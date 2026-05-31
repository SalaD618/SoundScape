package com.example.soundscape.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.soundscape.data.local.dao.FavoriteArtistDao
import com.example.soundscape.data.local.databse.SoundScapeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "soundscape_settings")

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSoundScapeDatabase(
        @ApplicationContext context: Context
    ): SoundScapeDatabase {
        return Room.databaseBuilder(
            context,
            SoundScapeDatabase::class.java,
            "soundscape_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteArtistDao(db: SoundScapeDatabase): FavoriteArtistDao {
        return db.favoriteArtistDao()
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }
}
