package com.plexwatch.domain.usecase.server

import com.plexwatch.domain.model.PlexServer
import com.plexwatch.domain.repository.ServerRepository
import javax.inject.Inject

class SelectServerUseCase
    @Inject
    constructor(
        private val serverRepository: ServerRepository,
    ) {
        suspend operator fun invoke(server: PlexServer) {
            serverRepository.selectServer(server)
        }
    }
