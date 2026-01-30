package com.plexwatch.data.api

import com.plexwatch.data.api.dto.LibrarySectionsResponse
import com.plexwatch.data.api.dto.MediaContainerResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PlexMediaApi {
    @GET("library/sections")
    suspend fun getLibrarySections(
        @Header("X-Plex-Token") token: String,
        @Header("Accept") accept: String = "application/json",
    ): LibrarySectionsResponse

    @GET("library/sections/{key}/all")
    suspend fun getLibraryContent(
        @Path("key") libraryKey: String,
        @Header("X-Plex-Token") token: String,
        @Query("type") type: Int? = null,
        @Header("Accept") accept: String = "application/json",
    ): MediaContainerResponse

    @GET("library/metadata/{id}/children")
    suspend fun getChildren(
        @Path("id") metadataId: String,
        @Header("X-Plex-Token") token: String,
        @Header("Accept") accept: String = "application/json",
    ): MediaContainerResponse
}
