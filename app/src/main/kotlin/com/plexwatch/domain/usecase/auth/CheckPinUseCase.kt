package com.plexwatch.domain.usecase.auth

import com.plexwatch.domain.model.PlexUser
import com.plexwatch.domain.repository.AuthRepository
import javax.inject.Inject

class CheckPinUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(pinId: Long): Result<PlexUser?> {
            return authRepository.checkPin(pinId)
        }
    }
