package com.plexwatch.presentation.ui.servers

import com.plexwatch.domain.model.PlexServer

sealed interface ServersUiState {
    data object Loading : ServersUiState

    data class Success(
        val servers: List<PlexServer>,
    ) : ServersUiState

    data class Error(
        val message: String,
    ) : ServersUiState
}
