package com.plexwatch.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.model.PlexPin
import com.plexwatch.domain.usecase.auth.CheckPinUseCase
import com.plexwatch.domain.usecase.auth.CreatePinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val createPinUseCase: CreatePinUseCase,
        private val checkPinUseCase: CheckPinUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
        val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

        private var pollingJob: Job? = null
        private var countdownJob: Job? = null

        init {
            startLoginFlow()
        }

        fun startLoginFlow() {
            cancelJobs()
            _uiState.value = LoginUiState.Loading

            viewModelScope.launch {
                createPinUseCase()
                    .onSuccess { pin ->
                        startPolling(pin)
                        startCountdown(pin)
                    }
                    .onFailure { error ->
                        _uiState.value =
                            LoginUiState.Error(
                                error.message ?: "Failed to generate PIN",
                            )
                    }
            }
        }

        fun retry() {
            startLoginFlow()
        }

        private fun startPolling(pin: PlexPin) {
            pollingJob =
                viewModelScope.launch {
                    while (true) {
                        delay(POLLING_INTERVAL_MS)

                        checkPinUseCase(pin.id)
                            .onSuccess { user ->
                                if (user != null) {
                                    cancelJobs()
                                    _uiState.value = LoginUiState.Authenticated(user.username)
                                    return@launch
                                }
                            }
                            .onFailure {
                                // Continue polling on failure, will eventually expire
                            }
                    }
                }
        }

        private fun startCountdown(pin: PlexPin) {
            countdownJob =
                viewModelScope.launch {
                    val expiresAt = pin.expiresAt
                    while (true) {
                        val now = System.currentTimeMillis()
                        val remainingMs = expiresAt - now
                        val remainingSeconds = (remainingMs / 1000).toInt()

                        if (remainingSeconds <= 0) {
                            cancelJobs()
                            _uiState.value = LoginUiState.Expired
                            return@launch
                        }

                        _uiState.value =
                            LoginUiState.ShowingPin(
                                pin = pin,
                                remainingSeconds = remainingSeconds,
                            )

                        delay(COUNTDOWN_INTERVAL_MS)
                    }
                }
        }

        private fun cancelJobs() {
            pollingJob?.cancel()
            pollingJob = null
            countdownJob?.cancel()
            countdownJob = null
        }

        override fun onCleared() {
            super.onCleared()
            cancelJobs()
        }

        companion object {
            private const val POLLING_INTERVAL_MS = 2500L
            private const val COUNTDOWN_INTERVAL_MS = 1000L
        }
    }
