package com.plexwatch.data.repository

import com.plexwatch.data.api.PlexServerApi
import com.plexwatch.data.local.TokenStorage
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
        private val tokenStorage: TokenStorage,
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
                        .flatMap { resource ->
                            resource.connections?.map { connection ->
                                PlexServer(
                                    id = resource.clientIdentifier,
                                    name = resource.name,
                                    address = connection.address,
                                    port = connection.port,
                                    isLocal = connection.local,
                                    isOwned = resource.owned,
                                )
                            } ?: emptyList()
                        }

                _servers.value = servers
                servers
            }

        override suspend fun selectServer(server: PlexServer) {
            _selectedServer.value = server
        }

        override fun getSelectedServer(): Flow<PlexServer?> = _selectedServer.asStateFlow()
    }
