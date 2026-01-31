package com.plexwatch.presentation.ui.tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.foundation.rotary.rotaryScrollable
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.plexwatch.R
import com.plexwatch.domain.model.Track

@Composable
fun TracksScreen(
    onTrackClick: (trackId: String) -> Unit,
    viewModel: TracksViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberScalingLazyListState()
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
    ) {
        when (val state = uiState) {
            is TracksUiState.Loading -> {
                LoadingContent()
            }
            is TracksUiState.Success -> {
                TracksContent(
                    tracks = state.tracks,
                    onTrackClick = { trackId ->
                        viewModel.onTrackClick(trackId)
                        onTrackClick(trackId)
                    },
                    listState = listState,
                    focusRequester = focusRequester,
                )
            }
            is TracksUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    onRetry = viewModel::retry,
                    listState = listState,
                    focusRequester = focusRequester,
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            CircularProgressIndicator()
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                text = stringResource(R.string.tracks_loading),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun TracksContent(
    tracks: List<Track>,
    onTrackClick: (trackId: String) -> Unit,
    listState: androidx.wear.compose.foundation.lazy.ScalingLazyListState,
    focusRequester: FocusRequester,
) {
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ScalingLazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .rotaryScrollable(
                    behavior = RotaryScrollableDefaults.behavior(listState),
                    focusRequester = focusRequester,
                )
                .focusRequester(focusRequester),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = stringResource(R.string.tracks_title),
                style = MaterialTheme.typography.title3,
                textAlign = TextAlign.Center,
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (tracks.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.tracks_empty),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurfaceVariant,
                )
            }
        } else {
            items(tracks) { track ->
                TrackChip(
                    track = track,
                    onClick = { onTrackClick(track.id) },
                )
            }
        }
    }
}

@Composable
private fun TrackChip(
    track: Track,
    onClick: () -> Unit,
) {
    Chip(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        label = {
            Text(
                text = "${track.trackNumber}. ${track.title}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        secondaryLabel = {
            Text(
                text = formatDuration(track.duration),
                maxLines = 1,
            )
        },
        colors = ChipDefaults.secondaryChipColors(),
    )
}

private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    listState: androidx.wear.compose.foundation.lazy.ScalingLazyListState,
    focusRequester: FocusRequester,
) {
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ScalingLazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .rotaryScrollable(
                    behavior = RotaryScrollableDefaults.behavior(listState),
                    focusRequester = focusRequester,
                )
                .focusRequester(focusRequester),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            Text(
                text = message,
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error,
            )
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            Button(onClick = onRetry) {
                Text(stringResource(R.string.common_retry))
            }
        }
    }
}
