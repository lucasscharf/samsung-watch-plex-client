package com.plexwatch.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun PlexWatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = PlexWatchColors,
        typography = PlexWatchTypography,
        content = content,
    )
}
