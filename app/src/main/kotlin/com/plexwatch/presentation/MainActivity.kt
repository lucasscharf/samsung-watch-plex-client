package com.plexwatch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.plexwatch.presentation.navigation.PlexWatchNavHost
import com.plexwatch.presentation.theme.PlexWatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlexWatchTheme {
                PlexWatchNavHost()
            }
        }
    }
}
