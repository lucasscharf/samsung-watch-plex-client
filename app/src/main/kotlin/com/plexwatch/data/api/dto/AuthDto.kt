package com.plexwatch.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PinResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "code") val code: String,
    @Json(name = "expiresIn") val expiresIn: Int,
    @Json(name = "expiresAt") val expiresAt: String? = null,
    @Json(name = "createdAt") val createdAt: String? = null,
    @Json(name = "authToken") val authToken: String? = null,
    @Json(name = "trusted") val trusted: Boolean? = null,
    @Json(name = "clientIdentifier") val clientIdentifier: String? = null,
    @Json(name = "product") val product: String? = null,
    @Json(name = "qr") val qr: String? = null,
    @Json(name = "newRegistration") val newRegistration: Boolean? = null,
    @Json(name = "location") val location: LocationDto? = null,
)

@JsonClass(generateAdapter = true)
data class LocationDto(
    @Json(name = "code") val code: String? = null,
    @Json(name = "country") val country: String? = null,
    @Json(name = "city") val city: String? = null,
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "uuid") val uuid: String,
    @Json(name = "username") val username: String,
    @Json(name = "email") val email: String,
    @Json(name = "thumb") val thumb: String?,
)
