package com.plexwatch.presentation.ui.albums

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.repository.LibraryRepository
import com.plexwatch.domain.usecase.library.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel
    @Inject
    constructor(
        private val getAlbumsUseCase: GetAlbumsUseCase,
        private val libraryRepository: LibraryRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val artistId: String = checkNotNull(savedStateHandle["artistId"])

        private val _uiState = MutableStateFlow<AlbumsUiState>(AlbumsUiState.Loading)
        val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

        init {
            syncAndLoadAlbums()
        }

        private fun syncAndLoadAlbums() {
            viewModelScope.launch {
                _uiState.value = AlbumsUiState.Loading
                libraryRepository.syncArtistAlbums(artistId)
                    .onFailure { error ->
                        _uiState.value =
                            AlbumsUiState.Error(
                                error.message ?: "Failed to sync albums",
                            )
                        return@launch
                    }

                getAlbumsUseCase(artistId)
                    .onStart {
                        _uiState.value = AlbumsUiState.Loading
                    }
                    .catch { error ->
                        _uiState.value =
                            AlbumsUiState.Error(
                                error.message ?: "Failed to load albums",
                            )
                    }
                    .collect { albums ->
                        _uiState.value = AlbumsUiState.Success(albums)
                    }
            }
        }

        fun refresh() {
            viewModelScope.launch {
                val currentState = _uiState.value
                if (currentState is AlbumsUiState.Success) {
                    _uiState.value = currentState.copy(isRefreshing = true)
                }

                libraryRepository.refreshArtistAlbumsCache(artistId)
                    .onFailure { error ->
                        _uiState.value =
                            AlbumsUiState.Error(
                                error.message ?: "Failed to refresh albums",
                            )
                    }
                    .onSuccess {
                        if (currentState is AlbumsUiState.Success) {
                            _uiState.value = currentState.copy(isRefreshing = false)
                        }
                    }
            }
        }

        fun retry() {
            syncAndLoadAlbums()
        }
    }
