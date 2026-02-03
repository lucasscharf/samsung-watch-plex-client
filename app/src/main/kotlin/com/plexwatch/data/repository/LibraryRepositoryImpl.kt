package com.plexwatch.data.repository

import com.plexwatch.data.api.PlexMediaApi
import com.plexwatch.data.local.TokenStorageInterface
import com.plexwatch.data.local.db.dao.AlbumDao
import com.plexwatch.data.local.db.dao.ArtistDao
import com.plexwatch.data.local.db.dao.LibrarySyncDao
import com.plexwatch.data.local.db.dao.TrackDao
import com.plexwatch.data.local.db.entity.LibrarySyncEntity
import com.plexwatch.data.local.db.toDomain
import com.plexwatch.data.local.db.toEntity
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
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepositoryImpl
    @Inject
    constructor(
        private val mediaApi: PlexMediaApi,
        private val tokenStorage: TokenStorageInterface,
        private val serverRepository: ServerRepository,
        private val librarySyncDao: LibrarySyncDao,
        private val artistDao: ArtistDao,
        private val albumDao: AlbumDao,
        private val trackDao: TrackDao,
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

        override fun getArtists(libraryKey: String): Flow<List<Artist>> =
            artistDao.getByLibraryKey(libraryKey).map { entities ->
                entities.map { it.toDomain() }
            }

        override fun getAlbums(artistId: String): Flow<List<Album>> =
            albumDao.getByArtistId(artistId).map { entities ->
                entities.map { it.toDomain() }
            }

        override fun getAlbumTracks(albumId: String): Flow<List<Track>> =
            trackDao.getByAlbumId(albumId).map { entities ->
                entities.map { it.toDomain() }
            }

        override suspend fun isLibrarySynced(libraryKey: String): Boolean =
            librarySyncDao.getByLibraryKeyOnce(libraryKey) != null

        override suspend fun syncLibrary(
            libraryKey: String,
            serverId: String,
        ): Result<Unit> =
            runCatching {
                if (isLibrarySynced(libraryKey)) return@runCatching

                val artists = fetchArtistsFromApi(libraryKey)
                // Insert LibrarySyncEntity first (parent) before artists (children) due to FK constraint
                librarySyncDao.insert(
                    LibrarySyncEntity(
                        libraryKey = libraryKey,
                        serverId = serverId,
                        lastSyncedAt = System.currentTimeMillis(),
                        artistCount = artists.size,
                    ),
                )
                artistDao.insertAll(artists.map { it.toEntity(libraryKey) })
            }

        override suspend fun syncArtistAlbums(artistId: String): Result<Unit> =
            runCatching {
                val cachedAlbums = albumDao.getByArtistIdOnce(artistId)
                if (cachedAlbums.isNotEmpty()) return@runCatching

                val albums = fetchAlbumsFromApi(artistId)
                albumDao.insertAll(albums.map { it.toEntity(artistId) })
            }

        override suspend fun syncAlbumTracks(albumId: String): Result<Unit> =
            runCatching {
                val cachedTracks = trackDao.getByAlbumIdOnce(albumId)
                if (cachedTracks.isNotEmpty()) return@runCatching

                val tracks = fetchTracksFromApi(albumId)
                trackDao.insertAll(tracks.map { it.toEntity(albumId) })
            }

        override suspend fun refreshLibraryCache(
            libraryKey: String,
            serverId: String,
        ): Result<Unit> =
            runCatching {
                librarySyncDao.deleteByLibraryKey(libraryKey)
                val artists = fetchArtistsFromApi(libraryKey)
                // Insert LibrarySyncEntity first (parent) before artists (children) due to FK constraint
                librarySyncDao.insert(
                    LibrarySyncEntity(
                        libraryKey = libraryKey,
                        serverId = serverId,
                        lastSyncedAt = System.currentTimeMillis(),
                        artistCount = artists.size,
                    ),
                )
                artistDao.insertAll(artists.map { it.toEntity(libraryKey) })
            }

        override suspend fun refreshArtistAlbumsCache(artistId: String): Result<Unit> =
            runCatching {
                albumDao.deleteByArtistId(artistId)
                val albums = fetchAlbumsFromApi(artistId)
                albumDao.insertAll(albums.map { it.toEntity(artistId) })
            }

        override suspend fun refreshAlbumTracksCache(albumId: String): Result<Unit> =
            runCatching {
                trackDao.deleteByAlbumId(albumId)
                val tracks = fetchTracksFromApi(albumId)
                trackDao.insertAll(tracks.map { it.toEntity(albumId) })
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

        private suspend fun fetchArtistsFromApi(libraryKey: String): List<Artist> {
            val token =
                tokenStorage.getAuthToken()
                    ?: throw IllegalStateException("Not authenticated")

            val response = mediaApi.getLibraryContent(libraryKey, token, type = 8)
            return response.mediaContainer.metadata?.map { metadata ->
                Artist(
                    id = metadata.ratingKey,
                    name = metadata.title,
                    thumbUri = metadata.thumb,
                    albumCount = metadata.childCount ?: 0,
                )
            } ?: emptyList()
        }

        private suspend fun fetchAlbumsFromApi(artistId: String): List<Album> {
            val token =
                tokenStorage.getAuthToken()
                    ?: throw IllegalStateException("Not authenticated")

            val response = mediaApi.getChildren(artistId, token)
            return response.mediaContainer.metadata?.map { metadata ->
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

        private suspend fun fetchTracksFromApi(albumId: String): List<Track> {
            val token =
                tokenStorage.getAuthToken()
                    ?: throw IllegalStateException("Not authenticated")

            val response = mediaApi.getChildren(albumId, token)
            return response.mediaContainer.metadata?.map { metadata ->
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
    }
