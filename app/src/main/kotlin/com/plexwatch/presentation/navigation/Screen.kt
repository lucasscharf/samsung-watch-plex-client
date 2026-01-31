package com.plexwatch.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object Login : Screen("login")

    data object Servers : Screen("servers")

    data object Libraries : Screen("libraries/{serverId}") {
        fun createRoute(serverId: String) = "libraries/$serverId"
    }

    data object Artists : Screen("artists/{libraryKey}") {
        fun createRoute(libraryKey: String) = "artists/$libraryKey"
    }

    data object Albums : Screen("albums/{artistId}") {
        fun createRoute(artistId: String) = "albums/$artistId"
    }

    data object Tracks : Screen("tracks/{albumId}") {
        fun createRoute(albumId: String) = "tracks/$albumId"
    }

    data object NowPlaying : Screen("now_playing")
}
