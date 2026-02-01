package com.plexwatch.data.repository

import app.cash.turbine.test
import com.plexwatch.data.api.PlexMediaApi
import com.plexwatch.data.api.dto.DirectoryDto
import com.plexwatch.data.api.dto.LibrarySectionsContainer
import com.plexwatch.data.api.dto.LibrarySectionsResponse
import com.plexwatch.data.api.dto.MediaContainer
import com.plexwatch.data.api.dto.MediaContainerResponse
import com.plexwatch.data.api.dto.MediaInfoDto
import com.plexwatch.data.api.dto.MetadataDto
import com.plexwatch.data.api.dto.PartDto
import com.plexwatch.data.local.FakeTokenStorage
import com.plexwatch.domain.model.LibraryType
import com.plexwatch.domain.repository.ServerRepository
import com.plexwatch.util.TestFixtures
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LibraryRepositoryImplTest {
    private lateinit var mediaApi: PlexMediaApi
    private lateinit var tokenStorage: FakeTokenStorage
    private lateinit var serverRepository: ServerRepository
    private lateinit var repository: LibraryRepositoryImpl

    @Before
    fun setUp() {
        mediaApi = mockk()
        tokenStorage = FakeTokenStorage()
        serverRepository = mockk()
        repository = LibraryRepositoryImpl(mediaApi, tokenStorage, serverRepository)
    }

    @Test
    fun `getLibraries returns empty list initially`() =
        runTest {
            repository.getLibraries("server-123").test {
                assertEquals(emptyList<Any>(), awaitItem())
            }
        }

    @Test
    fun `refreshLibraries maps directories to libraries`() =
        runTest {
            val response =
                LibrarySectionsResponse(
                    mediaContainer =
                        LibrarySectionsContainer(
                            directories =
                                listOf(
                                    DirectoryDto(
                                        key = "1",
                                        title = "Music Library",
                                        type = "artist",
                                        uuid = "lib-uuid-1",
                                        art = "/library/art",
                                        thumb = null,
                                    ),
                                    DirectoryDto(
                                        key = "2",
                                        title = "Movies",
                                        type = "movie",
                                        uuid = "lib-uuid-2",
                                        art = null,
                                        thumb = null,
                                    ),
                                ),
                        ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { mediaApi.getLibrarySections(any(), any()) } returns response

            val result = repository.refreshLibraries("server-123")

            assertTrue(result.isSuccess)
            val libraries = result.getOrNull()!!
            assertEquals(2, libraries.size)
            assertEquals("Music Library", libraries[0].title)
            assertEquals(LibraryType.MUSIC, libraries[0].type)
            assertEquals("Movies", libraries[1].title)
            assertEquals(LibraryType.MOVIES, libraries[1].type)
        }

    @Test
    fun `refreshLibraries uses uuid as id when available`() =
        runTest {
            val response =
                LibrarySectionsResponse(
                    mediaContainer =
                        LibrarySectionsContainer(
                            directories =
                                listOf(
                                    DirectoryDto(
                                        key = "1",
                                        title = "Library",
                                        type = "artist",
                                        uuid = "custom-uuid",
                                        art = null,
                                        thumb = null,
                                    ),
                                ),
                        ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { mediaApi.getLibrarySections(any(), any()) } returns response

            val result = repository.refreshLibraries("server-123")

            assertTrue(result.isSuccess)
            assertEquals("custom-uuid", result.getOrNull()!![0].id)
        }

    @Test
    fun `refreshLibraries uses key as id when uuid is null`() =
        runTest {
            val response =
                LibrarySectionsResponse(
                    mediaContainer =
                        LibrarySectionsContainer(
                            directories =
                                listOf(
                                    DirectoryDto(
                                        key = "fallback-key",
                                        title = "Library",
                                        type = "artist",
                                        uuid = null,
                                        art = null,
                                        thumb = null,
                                    ),
                                ),
                        ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { mediaApi.getLibrarySections(any(), any()) } returns response

            val result = repository.refreshLibraries("server-123")

            assertTrue(result.isSuccess)
            assertEquals("fallback-key", result.getOrNull()!![0].id)
        }

    @Test
    fun `getArtists uses type 8 for artist`() =
        runTest {
            val response =
                MediaContainerResponse(
                    mediaContainer =
                        MediaContainer(
                            size = 2,
                            metadata =
                                listOf(
                                    MetadataDto(
                                        ratingKey = "artist-1",
                                        key = "/library/artist-1",
                                        title = "Artist One",
                                        type = "artist",
                                        thumb = "/thumb1",
                                        art = null,
                                        parentTitle = null,
                                        grandparentTitle = null,
                                        year = null,
                                        duration = null,
                                        index = null,
                                        leafCount = 5,
                                        media = null,
                                    ),
                                ),
                        ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { mediaApi.getLibraryContent("1", "test-token", type = 8, any()) } returns response

            val result = repository.getArtists("1")

            assertTrue(result.isSuccess)
            val artists = result.getOrNull()!!
            assertEquals(1, artists.size)
            assertEquals("Artist One", artists[0].name)
            assertEquals(5, artists[0].albumCount)
        }

    @Test
    fun `getStreamUrl constructs correct URL`() =
        runTest {
            val server =
                TestFixtures.createPlexServer(
                    baseUrl = "https://45-79-210-125.abc123.plex.direct:8443",
                )
            val track = TestFixtures.createTrack(mediaKey = "/library/parts/123/file.mp3")

            every { serverRepository.getSelectedServer() } returns flowOf(server)
            tokenStorage.setAuthToken("test-token")

            val result = repository.getStreamUrl(track)

            assertEquals(
                "https://45-79-210-125.abc123.plex.direct:8443/library/parts/123/file.mp3?X-Plex-Token=test-token",
                result,
            )
        }

    @Test
    fun `getStreamUrl throws when no server selected`() =
        runTest {
            every { serverRepository.getSelectedServer() } returns flowOf(null)
            tokenStorage.setAuthToken("test-token")

            val track = TestFixtures.createTrack()

            try {
                repository.getStreamUrl(track)
                assertTrue("Expected exception", false)
            } catch (e: IllegalStateException) {
                assertEquals("No server selected", e.message)
            }
        }

    @Test
    fun `getAlbumTracks maps metadata to tracks`() =
        runTest {
            val response =
                MediaContainerResponse(
                    mediaContainer =
                        MediaContainer(
                            size = 1,
                            metadata =
                                listOf(
                                    MetadataDto(
                                        ratingKey = "track-1",
                                        key = "/library/track-1",
                                        title = "Track Title",
                                        type = "track",
                                        thumb = "/thumb",
                                        art = null,
                                        parentTitle = "Album Title",
                                        grandparentTitle = "Artist Name",
                                        year = null,
                                        duration = 180000L,
                                        index = 1,
                                        leafCount = null,
                                        media =
                                            listOf(
                                                MediaInfoDto(
                                                    id = 1L,
                                                    parts =
                                                        listOf(
                                                            PartDto(
                                                                id = 1L,
                                                                key = "/library/parts/1/file.mp3",
                                                                file = null,
                                                            ),
                                                        ),
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { mediaApi.getChildren("album-123", "test-token", any()) } returns response

            val result = repository.getAlbumTracks("album-123")

            assertTrue(result.isSuccess)
            val tracks = result.getOrNull()!!
            assertEquals(1, tracks.size)
            assertEquals("Track Title", tracks[0].title)
            assertEquals("Artist Name", tracks[0].artistName)
            assertEquals("Album Title", tracks[0].albumTitle)
            assertEquals(180000L, tracks[0].duration)
            assertEquals(1, tracks[0].trackNumber)
            assertEquals("/library/parts/1/file.mp3", tracks[0].mediaKey)
        }
}
