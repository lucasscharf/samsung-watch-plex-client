package com.plexwatch.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.AuthRepository
import com.plexwatch.domain.repository.PlaybackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val playbackRepository: PlaybackRepository,
    ) : ViewModel() {
        val uiState: StateFlow<HomeUiState> =
            authRepository.isAuthenticated
                .map { isAuthenticated ->
                    if (isAuthenticated) {
                        HomeUiState.Authenticated
                    } else {
                        HomeUiState.NotAuthenticated
                    }
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = HomeUiState.Loading,
                )

        private val _navigateToNowPlaying = MutableSharedFlow<Unit>()
        val navigateToNowPlaying: SharedFlow<Unit> = _navigateToNowPlaying.asSharedFlow()

        val currentTrack: StateFlow<Track?> = playbackRepository.currentTrack

        init {
            viewModelScope.launch {
                // Small delay to ensure MediaController is connected
                delay(500)
                if (playbackRepository.currentTrack.value != null) {
                    _navigateToNowPlaying.emit(Unit)
                }
            }
        }
    }

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data object NotAuthenticated : HomeUiState

    data object Authenticated : HomeUiState
}
