package com.plexwatch.domain.model

data class PlexServer(
    val id: String,
    val name: String,
    val baseUrl: String,
    val isOwned: Boolean,
)
