package com.plexwatch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.plexwatch.presentation.ui.home.HomeScreen
import com.plexwatch.presentation.ui.login.LoginScreen

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
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Servers.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
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
