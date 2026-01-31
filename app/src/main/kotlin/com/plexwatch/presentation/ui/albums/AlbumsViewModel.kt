package com.plexwatch.presentation.ui.albums

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.usecase.library.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel
    @Inject
    constructor(
        private val getAlbumsUseCase: GetAlbumsUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val artistId: String = checkNotNull(savedStateHandle["artistId"])

        private val _uiState = MutableStateFlow<AlbumsUiState>(AlbumsUiState.Loading)
        val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

        init {
            loadAlbums()
        }

        private fun loadAlbums() {
            viewModelScope.launch {
                _uiState.value = AlbumsUiState.Loading
                getAlbumsUseCase(artistId)
                    .onSuccess { albums ->
                        _uiState.value = AlbumsUiState.Success(albums)
                    }
                    .onFailure { error ->
                        _uiState.value =
                            AlbumsUiState.Error(
                                error.message ?: "Failed to load albums",
                            )
                    }
            }
        }

        fun retry() {
            loadAlbums()
        }
    }
