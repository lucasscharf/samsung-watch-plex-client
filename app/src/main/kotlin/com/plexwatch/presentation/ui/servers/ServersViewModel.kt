package com.plexwatch.presentation.ui.servers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.model.PlexServer
import com.plexwatch.domain.usecase.server.GetServersUseCase
import com.plexwatch.domain.usecase.server.RefreshServersUseCase
import com.plexwatch.domain.usecase.server.SelectServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServersViewModel
    @Inject
    constructor(
        private val getServersUseCase: GetServersUseCase,
        private val refreshServersUseCase: RefreshServersUseCase,
        private val selectServerUseCase: SelectServerUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<ServersUiState>(ServersUiState.Loading)
        val uiState: StateFlow<ServersUiState> = _uiState.asStateFlow()

        init {
            loadServers()
        }

        private fun loadServers() {
            viewModelScope.launch {
                getServersUseCase()
                    .onStart {
                        _uiState.value = ServersUiState.Loading
                    }
                    .catch { error ->
                        _uiState.value =
                            ServersUiState.Error(
                                error.message ?: "Failed to load servers",
                            )
                    }
                    .collect { servers ->
                        if (servers.isEmpty()) {
                            refreshServers()
                        } else {
                            _uiState.value = ServersUiState.Success(servers)
                        }
                    }
            }
        }

        fun refreshServers() {
            viewModelScope.launch {
                _uiState.value = ServersUiState.Loading
                refreshServersUseCase()
                    .onSuccess { servers ->
                        _uiState.value = ServersUiState.Success(servers)
                    }
                    .onFailure { error ->
                        _uiState.value =
                            ServersUiState.Error(
                                error.message ?: "Failed to refresh servers",
                            )
                    }
            }
        }

        fun selectServer(
            server: PlexServer,
            onSelected: () -> Unit,
        ) {
            viewModelScope.launch {
                selectServerUseCase(server)
                onSelected()
            }
        }

        fun retry() {
            refreshServers()
        }
    }
