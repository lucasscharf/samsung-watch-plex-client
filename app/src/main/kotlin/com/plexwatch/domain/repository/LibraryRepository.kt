package com.plexwatch.domain.repository

import com.plexwatch.domain.model.Album
import com.plexwatch.domain.model.Artist
import com.plexwatch.domain.model.PlexLibrary
import com.plexwatch.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun getLibraries(serverId: String): Flow<List<PlexLibrary>>

    suspend fun refreshLibraries(serverId: String): Result<List<PlexLibrary>>

    suspend fun getArtists(libraryKey: String): Result<List<Artist>>

    suspend fun getAlbums(artistId: String): Result<List<Album>>

    suspend fun getAlbumTracks(albumId: String): Result<List<Track>>

    suspend fun getStreamUrl(track: Track): String
}
