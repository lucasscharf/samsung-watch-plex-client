package com.plexwatch.presentation.ui.tracks

import com.plexwatch.domain.model.Track

sealed interface TracksUiState {
    data object Loading : TracksUiState

    data class Success(
        val tracks: List<Track>,
        val isRefreshing: Boolean = false,
    ) : TracksUiState

    data class Error(
        val message: String,
    ) : TracksUiState
}
