package com.plexwatch.presentation.ui.libraries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.usecase.library.GetLibrariesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibrariesViewModel
    @Inject
    constructor(
        private val getLibrariesUseCase: GetLibrariesUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val serverId: String = checkNotNull(savedStateHandle["serverId"])

        private val _uiState = MutableStateFlow<LibrariesUiState>(LibrariesUiState.Loading)
        val uiState: StateFlow<LibrariesUiState> = _uiState.asStateFlow()

        init {
            loadLibraries()
        }

        private fun loadLibraries() {
            viewModelScope.launch {
                getLibrariesUseCase(serverId)
                    .onStart {
                        _uiState.value = LibrariesUiState.Loading
                    }
                    .catch { error ->
                        _uiState.value =
                            LibrariesUiState.Error(
                                error.message ?: "Failed to load libraries",
                            )
                    }
                    .collect { libraries ->
                        _uiState.value = LibrariesUiState.Success(libraries)
                    }
            }
        }

        fun retry() {
            loadLibraries()
        }
    }
