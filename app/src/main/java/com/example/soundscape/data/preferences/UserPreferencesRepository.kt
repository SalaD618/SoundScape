package com.example.soundscape.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val DEFAULT_GENRE_KEY = stringPreferencesKey("default_genre")
        val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    val isDarkMode: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[DARK_MODE_KEY] ?: false
    }

    val defaultGenre: Flow<String> = dataStore.data.map { prefs ->
        prefs[DEFAULT_GENRE_KEY] ?: "All"
    }

    val language: Flow<String> = dataStore.data.map { prefs ->
        prefs[LANGUAGE_KEY] ?: "English"
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun setDefaultGenre(genre: String) {
        dataStore.edit { prefs ->
            prefs[DEFAULT_GENRE_KEY] = genre
        }
    }

    suspend fun setLanguage(language: String) {
        dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language
        }
    }
}
