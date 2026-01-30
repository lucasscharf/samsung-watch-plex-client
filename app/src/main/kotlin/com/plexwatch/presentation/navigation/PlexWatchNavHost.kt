package com.plexwatch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.plexwatch.presentation.ui.home.HomeScreen

@Composable
fun PlexWatchNavHost() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToServers = {
                    navController.navigate(Screen.Servers.route)
                },
            )
        }

        composable(Screen.Login.route) {
            // LoginScreen - to be implemented
        }

        composable(Screen.Servers.route) {
            // ServersScreen - to be implemented
        }

        composable(Screen.Libraries.route) {
            // LibrariesScreen - to be implemented
        }

        composable(Screen.NowPlaying.route) {
            // NowPlayingScreen - to be implemented
        }
    }
}
