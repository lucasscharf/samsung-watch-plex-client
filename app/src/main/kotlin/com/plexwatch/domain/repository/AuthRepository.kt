package com.plexwatch.domain.repository

import com.plexwatch.domain.model.PlexPin
import com.plexwatch.domain.model.PlexUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isAuthenticated: Flow<Boolean>
    val currentUser: Flow<PlexUser?>

    suspend fun createPin(): Result<PlexPin>

    suspend fun checkPin(pinId: Long): Result<PlexUser?>

    suspend fun logout()

    suspend fun getAuthToken(): String?
}
