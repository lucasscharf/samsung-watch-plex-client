package com.plexwatch.domain.usecase.auth

import com.plexwatch.domain.model.PlexPin
import com.plexwatch.domain.repository.AuthRepository
import javax.inject.Inject

class CreatePinUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(): Result<PlexPin> {
            return authRepository.createPin()
        }
    }
