package com.example.soundscape.navigation

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {

    data object Home : Screen(
        route = "home",
        label = "Home",
        icon = Icons.Default.Home
    )

    data object Search : Screen(
        route = "search",
        label = "Search",
        icon = Icons.Default.Search
    )

    data object Favorites : Screen(
        route = "favorites",
        label = "Favorites",
        icon = Icons.Default.Favorite
    )

    data object Stats : Screen(
        route = "stats",
        label = "Stats",
        icon = Icons.Default.BarChart
    )

    data object Settings : Screen(
        route = "settings",
        label = "Settings",
        icon = Icons.Default.Settings
    )

    data object Details : Screen(
        route = "details/{artistName}",
        label = "Details",
        icon = Icons.Default.Home
    ) {
        fun createRoute(artistName: String): String {
            return "details/${Uri.encode(artistName)}"
        }
    }
}