package com.plexwatch.presentation.ui.tracks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.LibraryRepository
import com.plexwatch.domain.usecase.library.GetTracksUseCase
import com.plexwatch.domain.usecase.playback.PlayTrackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TracksViewModel
    @Inject
    constructor(
        private val getTracksUseCase: GetTracksUseCase,
        private val playTrackUseCase: PlayTrackUseCase,
        private val libraryRepository: LibraryRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val albumId: String = checkNotNull(savedStateHandle["albumId"])

        private val _uiState = MutableStateFlow<TracksUiState>(TracksUiState.Loading)
        val uiState: StateFlow<TracksUiState> = _uiState.asStateFlow()

        private var currentTracks: List<Track> = emptyList()

        init {
            syncAndLoadTracks()
        }

        private fun syncAndLoadTracks() {
            viewModelScope.launch {
                _uiState.value = TracksUiState.Loading
                libraryRepository.syncAlbumTracks(albumId)
                    .onFailure { error ->
                        _uiState.value =
                            TracksUiState.Error(
                                error.message ?: "Failed to sync tracks",
                            )
                        return@launch
                    }

                getTracksUseCase(albumId)
                    .onStart {
                        _uiState.value = TracksUiState.Loading
                    }
                    .catch { error ->
                        _uiState.value =
                            TracksUiState.Error(
                                error.message ?: "Failed to load tracks",
                            )
                    }
                    .collect { tracks ->
                        currentTracks = tracks
                        _uiState.value = TracksUiState.Success(tracks)
                    }
            }
        }

        fun refresh() {
            viewModelScope.launch {
                val currentState = _uiState.value
                if (currentState is TracksUiState.Success) {
                    _uiState.value = currentState.copy(isRefreshing = true)
                }

                libraryRepository.refreshAlbumTracksCache(albumId)
                    .onFailure { error ->
                        _uiState.value =
                            TracksUiState.Error(
                                error.message ?: "Failed to refresh tracks",
                            )
                    }
                    .onSuccess {
                        if (currentState is TracksUiState.Success) {
                            _uiState.value = currentState.copy(isRefreshing = false)
                        }
                    }
            }
        }

        fun onTrackClick(trackId: String) {
            viewModelScope.launch {
                val index = currentTracks.indexOfFirst { it.id == trackId }
                if (index >= 0) {
                    playTrackUseCase(currentTracks, index)
                }
            }
        }

        fun retry() {
            syncAndLoadTracks()
        }
    }
