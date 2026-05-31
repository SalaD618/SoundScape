package com.example.soundscape.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


import android.net.Uri //pentru nume cu spații, e mai corect să encodăm.

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Search : Screen("search", "Search", Icons.Default.Search)
    data object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)


//    data object Details : Screen("details/{artistName}", "Details", Icons.Default.Home) {
//        fun createRoute(artistName: String): String {
//            return "details/${Uri.encode(artistName)}"
//        }
//    }
        data object Details : Screen(
            "details/{artistName}",
            "Details",
            Icons.Default.Home
        ) {
            fun createRoute(artistName: String): String {
                return "details/${Uri.encode(artistName)}"
            }
        }
}

