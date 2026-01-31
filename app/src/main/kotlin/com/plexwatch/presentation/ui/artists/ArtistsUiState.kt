package com.plexwatch.presentation.ui.artists

import com.plexwatch.domain.model.Artist

sealed interface ArtistsUiState {
    data object Loading : ArtistsUiState

    data class Success(
        val artists: List<Artist>,
    ) : ArtistsUiState

    data class Error(
        val message: String,
    ) : ArtistsUiState
}
