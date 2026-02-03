package com.plexwatch.domain.repository

import com.plexwatch.domain.model.Album
import com.plexwatch.domain.model.Artist
import com.plexwatch.domain.model.PlexLibrary
import com.plexwatch.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun getLibraries(serverId: String): Flow<List<PlexLibrary>>

    suspend fun refreshLibraries(serverId: String): Result<List<PlexLibrary>>

    fun getArtists(libraryKey: String): Flow<List<Artist>>

    fun getAlbums(artistId: String): Flow<List<Album>>

    fun getAlbumTracks(albumId: String): Flow<List<Track>>

    suspend fun isLibrarySynced(libraryKey: String): Boolean

    suspend fun syncLibrary(
        libraryKey: String,
        serverId: String,
    ): Result<Unit>

    suspend fun syncArtistAlbums(artistId: String): Result<Unit>

    suspend fun syncAlbumTracks(albumId: String): Result<Unit>

    suspend fun refreshLibraryCache(
        libraryKey: String,
        serverId: String,
    ): Result<Unit>

    suspend fun refreshArtistAlbumsCache(artistId: String): Result<Unit>

    suspend fun refreshAlbumTracksCache(albumId: String): Result<Unit>

    suspend fun getStreamUrl(track: Track): String
}
