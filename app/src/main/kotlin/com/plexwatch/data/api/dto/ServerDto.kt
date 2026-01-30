package com.plexwatch.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResourcesResponse(
    @Json(name = "name") val name: String,
    @Json(name = "clientIdentifier") val clientIdentifier: String,
    @Json(name = "provides") val provides: String,
    @Json(name = "owned") val owned: Boolean,
    @Json(name = "connections") val connections: List<ConnectionDto>?,
)

@JsonClass(generateAdapter = true)
data class ConnectionDto(
    @Json(name = "protocol") val protocol: String,
    @Json(name = "address") val address: String,
    @Json(name = "port") val port: Int,
    @Json(name = "uri") val uri: String,
    @Json(name = "local") val local: Boolean,
)
