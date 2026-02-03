package com.plexwatch.presentation.ui.artists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.repository.LibraryRepository
import com.plexwatch.domain.usecase.library.GetArtistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsViewModel
    @Inject
    constructor(
        private val getArtistsUseCase: GetArtistsUseCase,
        private val libraryRepository: LibraryRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val libraryKey: String = checkNotNull(savedStateHandle["libraryKey"])
        private val serverId: String = checkNotNull(savedStateHandle["serverId"])

        private val _uiState = MutableStateFlow<ArtistsUiState>(ArtistsUiState.Loading)
        val uiState: StateFlow<ArtistsUiState> = _uiState.asStateFlow()

        init {
            syncAndLoadArtists()
        }

        private fun syncAndLoadArtists() {
            viewModelScope.launch {
                _uiState.value = ArtistsUiState.Loading
                libraryRepository.syncLibrary(libraryKey, serverId)
                    .onFailure { error ->
                        _uiState.value =
                            ArtistsUiState.Error(
                                error.message ?: "Failed to sync library",
                            )
                        return@launch
                    }

                getArtistsUseCase(libraryKey)
                    .onStart {
                        _uiState.value = ArtistsUiState.Loading
                    }
                    .catch { error ->
                        _uiState.value =
                            ArtistsUiState.Error(
                                error.message ?: "Failed to load artists",
                            )
                    }
                    .collect { artists ->
                        _uiState.value = ArtistsUiState.Success(artists)
                    }
            }
        }

        fun refresh() {
            viewModelScope.launch {
                val currentState = _uiState.value
                if (currentState is ArtistsUiState.Success) {
                    _uiState.value = currentState.copy(isRefreshing = true)
                }

                libraryRepository.refreshLibraryCache(libraryKey, serverId)
                    .onFailure { error ->
                        _uiState.value =
                            ArtistsUiState.Error(
                                error.message ?: "Failed to refresh library",
                            )
                    }
                    .onSuccess {
                        if (currentState is ArtistsUiState.Success) {
                            _uiState.value = currentState.copy(isRefreshing = false)
                        }
                    }
            }
        }

        fun retry() {
            syncAndLoadArtists()
        }
    }
