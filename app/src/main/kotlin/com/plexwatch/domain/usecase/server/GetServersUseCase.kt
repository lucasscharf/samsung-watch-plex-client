package com.plexwatch.domain.usecase.server

import com.plexwatch.domain.model.PlexServer
import com.plexwatch.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetServersUseCase
    @Inject
    constructor(
        private val serverRepository: ServerRepository,
    ) {
        operator fun invoke(): Flow<List<PlexServer>> {
            return serverRepository.getServers()
        }
    }
