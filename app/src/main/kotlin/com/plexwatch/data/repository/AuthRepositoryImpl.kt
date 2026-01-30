package com.plexwatch.data.repository

import com.plexwatch.data.api.PlexAuthApi
import com.plexwatch.data.local.TokenStorageInterface
import com.plexwatch.domain.model.PlexPin
import com.plexwatch.domain.model.PlexUser
import com.plexwatch.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl
    @Inject
    constructor(
        private val authApi: PlexAuthApi,
        private val tokenStorage: TokenStorageInterface,
    ) : AuthRepository {
        override val isAuthenticated: Flow<Boolean> = tokenStorage.authToken.map { it != null }

        override val currentUser: Flow<PlexUser?> =
            tokenStorage.authToken.map { token ->
                if (token != null) {
                    try {
                        val response = authApi.getUser(token, tokenStorage.getClientId())
                        PlexUser(
                            id = response.id.toString(),
                            username = response.username,
                            email = response.email,
                            thumb = response.thumb,
                            authToken = token,
                        )
                    } catch (e: Exception) {
                        null
                    }
                } else {
                    null
                }
            }

        override suspend fun createPin(): Result<PlexPin> =
            runCatching {
                val response =
                    authApi.createPin(
                        clientId = tokenStorage.getClientId(),
                    )
                // 5 minutes default expiry
                PlexPin(
                    id = response.id,
                    code = response.code,
                    expiresAt = System.currentTimeMillis() + 300_000,
                )
            }

        override suspend fun checkPin(pinId: Long): Result<PlexUser?> =
            runCatching {
                val response = authApi.checkPin(pinId, tokenStorage.getClientId())
                if (response.authToken != null) {
                    tokenStorage.setAuthToken(response.authToken)
                    val userResponse = authApi.getUser(response.authToken, tokenStorage.getClientId())
                    tokenStorage.setUserId(userResponse.id.toString())
                    PlexUser(
                        id = userResponse.id.toString(),
                        username = userResponse.username,
                        email = userResponse.email,
                        thumb = userResponse.thumb,
                        authToken = response.authToken,
                    )
                } else {
                    null
                }
            }

        override suspend fun logout() {
            tokenStorage.clear()
        }

        override suspend fun getAuthToken(): String? = tokenStorage.getAuthToken()
    }
