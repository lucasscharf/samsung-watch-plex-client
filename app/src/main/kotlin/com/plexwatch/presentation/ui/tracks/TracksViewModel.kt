package com.plexwatch.presentation.ui.tracks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.usecase.library.GetTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TracksViewModel
    @Inject
    constructor(
        private val getTracksUseCase: GetTracksUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val albumId: String = checkNotNull(savedStateHandle["albumId"])

        private val _uiState = MutableStateFlow<TracksUiState>(TracksUiState.Loading)
        val uiState: StateFlow<TracksUiState> = _uiState.asStateFlow()

        init {
            loadTracks()
        }

        private fun loadTracks() {
            viewModelScope.launch {
                _uiState.value = TracksUiState.Loading
                getTracksUseCase(albumId)
                    .onSuccess { tracks ->
                        _uiState.value = TracksUiState.Success(tracks)
                    }
                    .onFailure { error ->
                        _uiState.value =
                            TracksUiState.Error(
                                error.message ?: "Failed to load tracks",
                            )
                    }
            }
        }

        fun retry() {
            loadTracks()
        }
    }
