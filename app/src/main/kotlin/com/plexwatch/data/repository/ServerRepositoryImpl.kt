package com.plexwatch.data.repository

import com.plexwatch.data.api.PlexServerApi
import com.plexwatch.data.local.TokenStorageInterface
import com.plexwatch.domain.model.PlexServer
import com.plexwatch.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerRepositoryImpl
    @Inject
    constructor(
        private val serverApi: PlexServerApi,
        private val tokenStorage: TokenStorageInterface,
    ) : ServerRepository {
        private val _servers = MutableStateFlow<List<PlexServer>>(emptyList())
        private val _selectedServer = MutableStateFlow<PlexServer?>(null)

        override fun getServers(): Flow<List<PlexServer>> = _servers.asStateFlow()

        override suspend fun refreshServers(): Result<List<PlexServer>> =
            runCatching {
                val token =
                    tokenStorage.getAuthToken()
                        ?: throw IllegalStateException("Not authenticated")

                val resources = serverApi.getResources(token, tokenStorage.getClientId())

                val servers =
                    resources
                        .filter { it.provides.contains("server") }
                        .mapNotNull { resource ->
                            // Find relay connection and use its URI directly
                            val relayConnection = resource.connections?.find { it.relay }
                            relayConnection?.let { connection ->
                                PlexServer(
                                    id = resource.clientIdentifier,
                                    name = resource.name,
                                    baseUrl = connection.uri,
                                    isOwned = resource.owned,
                                )
                            }
                        }

                _servers.value = servers
                servers
            }

        override suspend fun selectServer(server: PlexServer) {
            _selectedServer.value = server
        }

        override fun getSelectedServer(): Flow<PlexServer?> = _selectedServer.asStateFlow()
    }
