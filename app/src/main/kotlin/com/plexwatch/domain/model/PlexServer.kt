package com.plexwatch.domain.model

data class PlexServer(
    val id: String,
    val name: String,
    val address: String,
    val port: Int,
    val isLocal: Boolean,
    val isOwned: Boolean,
) {
    val baseUrl: String
        get() = "http://$address:$port"
}
