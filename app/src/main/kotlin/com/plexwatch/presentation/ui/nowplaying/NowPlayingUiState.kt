package com.plexwatch.presentation.ui.nowplaying

sealed interface NowPlayingUiState {
    data object Loading : NowPlayingUiState

    data object Empty : NowPlayingUiState

    data class Playing(
        val trackTitle: String,
        val artistName: String,
        val albumTitle: String,
        val thumbUri: String?,
        val isPlaying: Boolean,
        val progress: Long,
        val duration: Long,
        val hasNext: Boolean,
        val hasPrevious: Boolean,
    ) : NowPlayingUiState
}
