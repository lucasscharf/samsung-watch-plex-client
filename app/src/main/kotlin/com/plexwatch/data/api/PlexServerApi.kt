package com.plexwatch.data.api

import com.plexwatch.data.api.dto.ResourcesResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface PlexServerApi {
    @GET("api/v2/resources?includeHttps=1&includeRelay=1")
    suspend fun getResources(
        @Header("X-Plex-Token") token: String,
        @Header("X-Plex-Client-Identifier") clientId: String,
        @Header("Accept") accept: String = "application/json",
    ): List<ResourcesResponse>

    companion object {
        const val BASE_URL = "https://plex.tv/"
    }
}
