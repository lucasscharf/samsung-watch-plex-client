package com.plexwatch.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.plexwatch.R

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToServers: () -> Unit,
    onNavigateToNowPlaying: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()
    val listState = rememberScalingLazyListState()

    LaunchedEffect(Unit) {
        viewModel.navigateToNowPlaying.collect {
            onNavigateToNowPlaying()
        }
    }

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.Center,
        ) {
            item {
                Text(
                    text = stringResource(R.string.app_name),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                when (uiState) {
                    is HomeUiState.NotAuthenticated -> {
                        Button(
                            onClick = onNavigateToLogin,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Sign In")
                        }
                    }
                    is HomeUiState.Authenticated -> {
                        Button(
                            onClick = onNavigateToServers,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("My Servers")
                        }
                    }
                    is HomeUiState.Loading -> {
                        Text("Loading...")
                    }
                }
            }

            if (currentTrack != null) {
                item {
                    Chip(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onNavigateToNowPlaying,
                        label = {
                            Text(
                                text = stringResource(R.string.home_now_playing),
                            )
                        },
                        secondaryLabel = {
                            Text(
                                text = currentTrack?.title ?: "",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(android.R.drawable.ic_media_play),
                                contentDescription = null,
                                modifier = Modifier.size(ChipDefaults.IconSize),
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(),
                    )
                }
            }
        }
    }
}
