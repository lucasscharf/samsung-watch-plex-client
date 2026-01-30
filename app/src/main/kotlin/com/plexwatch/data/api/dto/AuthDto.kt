package com.plexwatch.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PinResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "code") val code: String,
    @Json(name = "expiresAt") val expiresAt: String?,
    @Json(name = "authToken") val authToken: String?,
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "uuid") val uuid: String,
    @Json(name = "username") val username: String,
    @Json(name = "email") val email: String,
    @Json(name = "thumb") val thumb: String?,
)
