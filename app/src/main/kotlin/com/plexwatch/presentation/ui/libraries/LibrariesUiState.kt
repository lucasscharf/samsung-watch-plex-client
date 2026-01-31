package com.plexwatch.presentation.ui.libraries

import com.plexwatch.domain.model.PlexLibrary

sealed interface LibrariesUiState {
    data object Loading : LibrariesUiState

    data class Success(
        val libraries: List<PlexLibrary>,
    ) : LibrariesUiState

    data class Error(
        val message: String,
    ) : LibrariesUiState
}
