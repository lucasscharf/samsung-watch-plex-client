package com.plexwatch.data.api

import com.plexwatch.data.api.dto.PinResponse
import com.plexwatch.data.api.dto.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PlexAuthApi {
    @FormUrlEncoded
    @POST("api/v2/pins")
    suspend fun createPin(
        @Field("strong") strong: Boolean = true,
        @Header("X-Plex-Product") product: String = PLEX_PRODUCT,
        @Header("X-Plex-Client-Identifier") clientId: String,
        @Header("Accept") accept: String = "application/json",
    ): PinResponse

    @GET("api/v2/pins/{id}")
    suspend fun checkPin(
        @Path("id") pinId: Long,
        @Header("X-Plex-Client-Identifier") clientId: String,
        @Header("Accept") accept: String = "application/json",
    ): PinResponse

    @GET("api/v2/user")
    suspend fun getUser(
        @Header("X-Plex-Token") token: String,
        @Header("X-Plex-Client-Identifier") clientId: String,
        @Header("Accept") accept: String = "application/json",
    ): UserResponse

    companion object {
        const val BASE_URL = "https://plex.tv/"
        const val PLEX_PRODUCT = "PlexWatch"
    }
}
