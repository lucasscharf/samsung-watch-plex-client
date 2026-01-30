package com.plexwatch.data.repository

import com.plexwatch.data.api.PlexMediaApi
import com.plexwatch.data.local.TokenStorage
import com.plexwatch.domain.model.Album
import com.plexwatch.domain.model.Artist
import com.plexwatch.domain.model.LibraryType
import com.plexwatch.domain.model.PlexLibrary
import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.LibraryRepository
import com.plexwatch.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepositoryImpl
    @Inject
    constructor(
        private val mediaApi: PlexMediaApi,
        private val tokenStorage: TokenStorage,
        private val serverRepository: ServerRepository,
    ) : LibraryRepository {
        private val _libraries = MutableStateFlow<Map<String, List<PlexLibrary>>>(emptyMap())

        override fun getLibraries(serverId: String): Flow<List<PlexLibrary>> {
            return MutableStateFlow(_libraries.value[serverId] ?: emptyList()).asStateFlow()
        }

        override suspend fun refreshLibraries(serverId: String): Result<List<PlexLibrary>> =
            runCatching {
                val token =
                    tokenStorage.getAuthToken()
                        ?: throw IllegalStateException("Not authenticated")

                val response = mediaApi.getLibrarySections(token)
                val libraries =
                    response.mediaContainer.directories?.map { directory ->
                        PlexLibrary(
                            id = directory.uuid ?: directory.key,
                            key = directory.key,
                            title = directory.title,
                            type = LibraryType.fromString(directory.type),
                            artUri = directory.art,
                        )
                    } ?: emptyList()

                _libraries.value = _libraries.value + (serverId to libraries)
                libraries
            }

        override suspend fun getArtists(libraryKey: String): Result<List<Artist>> =
            runCatching {
                val token =
                    tokenStorage.getAuthToken()
                        ?: throw IllegalStateException("Not authenticated")

                // Plex type 8 = artist
                val response = mediaApi.getLibraryContent(libraryKey, token, type = 8)
                response.mediaContainer.metadata?.map { metadata ->
                    Artist(
                        id = metadata.ratingKey,
                        name = metadata.title,
                        thumbUri = metadata.thumb,
                        albumCount = metadata.leafCount ?: 0,
                    )
                } ?: emptyList()
            }

        override suspend fun getAlbums(artistId: String): Result<List<Album>> =
            runCatching {
                val token =
                    tokenStorage.getAuthToken()
                        ?: throw IllegalStateException("Not authenticated")

                val response = mediaApi.getChildren(artistId, token)
                response.mediaContainer.metadata?.map { metadata ->
                    Album(
                        id = metadata.ratingKey,
                        title = metadata.title,
                        artistName = metadata.parentTitle ?: "",
                        year = metadata.year,
                        thumbUri = metadata.thumb,
                        trackCount = metadata.leafCount ?: 0,
                    )
                } ?: emptyList()
            }

        override suspend fun getAlbumTracks(albumId: String): Result<List<Track>> =
            runCatching {
                val token =
                    tokenStorage.getAuthToken()
                        ?: throw IllegalStateException("Not authenticated")

                val response = mediaApi.getChildren(albumId, token)
                response.mediaContainer.metadata?.map { metadata ->
                    Track(
                        id = metadata.ratingKey,
                        title = metadata.title,
                        artistName = metadata.grandparentTitle ?: "",
                        albumTitle = metadata.parentTitle ?: "",
                        duration = metadata.duration ?: 0,
                        trackNumber = metadata.index ?: 0,
                        thumbUri = metadata.thumb,
                        mediaKey = metadata.media?.firstOrNull()?.parts?.firstOrNull()?.key ?: "",
                    )
                } ?: emptyList()
            }

        override suspend fun getStreamUrl(track: Track): String {
            val server =
                serverRepository.getSelectedServer().first()
                    ?: throw IllegalStateException("No server selected")
            val token =
                tokenStorage.getAuthToken()
                    ?: throw IllegalStateException("Not authenticated")

            return "${server.baseUrl}${track.mediaKey}?X-Plex-Token=$token"
        }
    }
