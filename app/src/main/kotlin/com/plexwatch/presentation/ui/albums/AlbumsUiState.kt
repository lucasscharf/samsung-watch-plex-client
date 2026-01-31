package com.plexwatch.presentation.ui.albums

import com.plexwatch.domain.model.Album

sealed interface AlbumsUiState {
    data object Loading : AlbumsUiState

    data class Success(
        val albums: List<Album>,
    ) : AlbumsUiState

    data class Error(
        val message: String,
    ) : AlbumsUiState
}
