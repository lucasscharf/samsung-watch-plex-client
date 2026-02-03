package com.plexwatch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.plexwatch.presentation.ui.albums.AlbumsScreen
import com.plexwatch.presentation.ui.artists.ArtistsScreen
import com.plexwatch.presentation.ui.home.HomeScreen
import com.plexwatch.presentation.ui.libraries.LibrariesScreen
import com.plexwatch.presentation.ui.login.LoginScreen
import com.plexwatch.presentation.ui.nowplaying.NowPlayingScreen
import com.plexwatch.presentation.ui.servers.ServersScreen
import com.plexwatch.presentation.ui.tracks.TracksScreen

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
                onNavigateToNowPlaying = {
                    navController.navigate(Screen.NowPlaying.route)
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
            ServersScreen(
                onServerSelected = { serverId ->
                    navController.navigate(Screen.Libraries.createRoute(serverId))
                },
            )
        }

        composable(Screen.Libraries.route) {
            LibrariesScreen(
                onLibraryClick = { libraryKey, serverId ->
                    navController.navigate(Screen.Artists.createRoute(libraryKey, serverId))
                },
            )
        }

        composable(Screen.Artists.route) {
            ArtistsScreen(
                onArtistClick = { artistId ->
                    navController.navigate(Screen.Albums.createRoute(artistId))
                },
            )
        }

        composable(Screen.Albums.route) {
            AlbumsScreen(
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.Tracks.createRoute(albumId))
                },
            )
        }

        composable(Screen.Tracks.route) {
            TracksScreen(
                onTrackClick = { _ ->
                    // Phase 4: Navigate to NowPlaying
                    navController.navigate(Screen.NowPlaying.route)
                },
            )
        }

        composable(Screen.NowPlaying.route) {
            NowPlayingScreen()
        }
    }
}
