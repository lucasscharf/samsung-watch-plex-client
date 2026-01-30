package com.plexwatch.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
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
    }

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data object NotAuthenticated : HomeUiState

    data object Authenticated : HomeUiState
}
