package com.plexwatch.presentation.ui.nowplaying

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import coil.compose.AsyncImage
import com.plexwatch.R

@Composable
fun NowPlayingScreen(viewModel: NowPlayingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
    ) {
        when (val state = uiState) {
            is NowPlayingUiState.Loading -> {
                LoadingContent()
            }
            is NowPlayingUiState.Empty -> {
                EmptyContent()
            }
            is NowPlayingUiState.Playing -> {
                PlayingContent(
                    state = state,
                    onPlayPauseClick = viewModel::onPlayPauseClick,
                    onSkipNextClick = viewModel::onSkipNextClick,
                    onSkipPreviousClick = viewModel::onSkipPreviousClick,
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.now_playing_empty),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun PlayingContent(
    state: NowPlayingUiState.Playing,
    onPlayPauseClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onSkipPreviousClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (state.thumbUri != null) {
            AsyncImage(
                model = state.thumbUri,
                contentDescription = state.albumTitle,
                modifier =
                    Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = state.trackTitle,
            style = MaterialTheme.typography.title3,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = state.artistName,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProgressIndicator(
            progress = state.progress,
            duration = state.duration,
        )

        Spacer(modifier = Modifier.height(12.dp))

        PlaybackControls(
            isPlaying = state.isPlaying,
            hasNext = state.hasNext,
            hasPrevious = state.hasPrevious,
            onPlayPauseClick = onPlayPauseClick,
            onSkipNextClick = onSkipNextClick,
            onSkipPreviousClick = onSkipPreviousClick,
        )
    }
}

@Composable
private fun ProgressIndicator(
    progress: Long,
    duration: Long,
) {
    val progressFraction = if (duration > 0) progress.toFloat() / duration else 0f

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        androidx.compose.foundation.layout.Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(4.dp),
        ) {
            androidx.compose.foundation.Canvas(
                modifier = Modifier.fillMaxSize(),
            ) {
                val trackColor = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.3f)
                val progressColor = androidx.compose.ui.graphics.Color.White

                drawRect(color = trackColor)
                drawRect(
                    color = progressColor,
                    size = size.copy(width = size.width * progressFraction),
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatDuration(progress),
                style = MaterialTheme.typography.caption3,
                color = MaterialTheme.colors.onSurfaceVariant,
            )
            Text(
                text = formatDuration(duration),
                style = MaterialTheme.typography.caption3,
                color = MaterialTheme.colors.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PlaybackControls(
    isPlaying: Boolean,
    hasNext: Boolean,
    hasPrevious: Boolean,
    onPlayPauseClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onSkipPreviousClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompactButton(
            onClick = onSkipPreviousClick,
            enabled = hasPrevious,
            colors = ButtonDefaults.secondaryButtonColors(),
        ) {
            Icon(
                painter =
                    androidx.compose.ui.res.painterResource(
                        android.R.drawable.ic_media_previous,
                    ),
                contentDescription = stringResource(R.string.now_playing_previous),
                modifier = Modifier.size(20.dp),
            )
        }

        Button(
            onClick = onPlayPauseClick,
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                painter =
                    androidx.compose.ui.res.painterResource(
                        if (isPlaying) {
                            android.R.drawable.ic_media_pause
                        } else {
                            android.R.drawable.ic_media_play
                        },
                    ),
                contentDescription =
                    stringResource(
                        if (isPlaying) R.string.now_playing_pause else R.string.now_playing_play,
                    ),
                modifier = Modifier.size(24.dp),
            )
        }

        CompactButton(
            onClick = onSkipNextClick,
            enabled = hasNext,
            colors = ButtonDefaults.secondaryButtonColors(),
        ) {
            Icon(
                painter =
                    androidx.compose.ui.res.painterResource(
                        android.R.drawable.ic_media_next,
                    ),
                contentDescription = stringResource(R.string.now_playing_next),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
