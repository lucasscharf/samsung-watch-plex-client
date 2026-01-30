package com.plexwatch.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

// Plex brand colors
val PlexOrange = Color(0xFFE5A00D)
val PlexOrangeDark = Color(0xFFCC8400)
val PlexBackground = Color(0xFF1F1F1F)
val PlexSurface = Color(0xFF282828)

val PlexWatchColors =
    Colors(
        primary = PlexOrange,
        primaryVariant = PlexOrangeDark,
        secondary = PlexOrange,
        secondaryVariant = PlexOrangeDark,
        background = PlexBackground,
        surface = PlexSurface,
        error = Color(0xFFCF6679),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White,
        onError = Color.Black,
    )
