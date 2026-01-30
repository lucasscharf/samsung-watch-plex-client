package com.plexwatch.domain.model

data class PlexPin(
    val id: Long,
    val code: String,
    val expiresAt: Long,
)

data class PlexUser(
    val id: String,
    val username: String,
    val email: String,
    val thumb: String?,
    val authToken: String,
)
