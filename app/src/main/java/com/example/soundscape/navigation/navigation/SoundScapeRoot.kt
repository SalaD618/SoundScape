package com.example.soundscape.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.soundscape.screen.favorites.FavoritesScreen
import com.example.soundscape.screen.home.HomeScreen
import com.example.soundscape.screen.search.SearchScreen
import com.example.soundscape.screen.settings.SettingsScreen
import com.example.soundscape.screen.details.DetailsScreen
import com.example.soundscape.screen.stats.StatsScreen
import com.example.soundscape.ui.theme.SoundScapeTheme
import com.example.soundscape.viewmodel.SettingsViewModel

@Composable
fun SoundScapeRoot() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsState by settingsViewModel.uiState.collectAsState()

    SoundScapeTheme(darkTheme = settingsState.isDarkMode) {
        val navController = rememberNavController()

        val bottomItems = listOf(
            Screen.Home,
            Screen.Search,
            Screen.Favorites,
            Screen.Stats,
            Screen.Settings
        )

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    bottomItems.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.label
                                )
                            },
                            label = {
                                Text(text = screen.label)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onArtistClick = { artistName ->
                            navController.navigate(
                                Screen.Details.createRoute(artistName)
                            )
                        }
                    )
                }

                composable(Screen.Search.route) {
                    SearchScreen(
                        onArtistClick = { artistName ->
                            navController.navigate(
                                Screen.Details.createRoute(
                                    artistName
                                )
                            )
                        }
                    )
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        onArtistClick = { artistName ->
                            navController.navigate(
                                Screen.Details.createRoute(artistName)
                            )
                        }
                    )
                }

                composable(Screen.Stats.route) {
                    StatsScreen()
                }

                composable(Screen.Settings.route) {
                    SettingsScreen()
                }

                composable(Screen.Details.route) { backStackEntry ->
                    val artistName =
                        backStackEntry.arguments?.getString("artistName").orEmpty()

                    DetailsScreen(
                        artistName = artistName
                    )
                }

            }
        }
    }
}
