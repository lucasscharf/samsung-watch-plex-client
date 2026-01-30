package com.plexwatch.presentation.ui.login

import com.plexwatch.domain.model.PlexPin

sealed interface LoginUiState {
    data object Loading : LoginUiState

    data class ShowingPin(
        val pin: PlexPin,
        val remainingSeconds: Int,
    ) : LoginUiState

    data class Authenticated(
        val username: String,
    ) : LoginUiState

    data object Expired : LoginUiState

    data class Error(
        val message: String,
    ) : LoginUiState
}
