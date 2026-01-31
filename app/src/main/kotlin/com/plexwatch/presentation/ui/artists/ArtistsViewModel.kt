package com.plexwatch.presentation.ui.artists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.usecase.library.GetArtistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsViewModel
    @Inject
    constructor(
        private val getArtistsUseCase: GetArtistsUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val libraryKey: String = checkNotNull(savedStateHandle["libraryKey"])

        private val _uiState = MutableStateFlow<ArtistsUiState>(ArtistsUiState.Loading)
        val uiState: StateFlow<ArtistsUiState> = _uiState.asStateFlow()

        init {
            loadArtists()
        }

        private fun loadArtists() {
            viewModelScope.launch {
                _uiState.value = ArtistsUiState.Loading
                getArtistsUseCase(libraryKey)
                    .onSuccess { artists ->
                        _uiState.value = ArtistsUiState.Success(artists)
                    }
                    .onFailure { error ->
                        _uiState.value =
                            ArtistsUiState.Error(
                                error.message ?: "Failed to load artists",
                            )
                    }
            }
        }

        fun retry() {
            loadArtists()
        }
    }
