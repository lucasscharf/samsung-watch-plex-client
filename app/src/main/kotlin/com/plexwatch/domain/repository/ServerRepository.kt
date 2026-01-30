package com.plexwatch.domain.repository

import com.plexwatch.domain.model.PlexServer
import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    fun getServers(): Flow<List<PlexServer>>

    suspend fun refreshServers(): Result<List<PlexServer>>

    suspend fun selectServer(server: PlexServer)

    fun getSelectedServer(): Flow<PlexServer?>
}
