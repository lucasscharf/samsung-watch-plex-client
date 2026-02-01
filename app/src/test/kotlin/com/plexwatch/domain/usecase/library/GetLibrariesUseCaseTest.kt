package com.plexwatch.domain.usecase.library

import app.cash.turbine.test
import com.plexwatch.domain.model.LibraryType
import com.plexwatch.domain.repository.LibraryRepository
import com.plexwatch.util.TestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetLibrariesUseCaseTest {
    private lateinit var libraryRepository: LibraryRepository
    private lateinit var useCase: GetLibrariesUseCase

    @Before
    fun setUp() {
        libraryRepository = mockk()
        useCase = GetLibrariesUseCase(libraryRepository)
    }

    @Test
    fun `invoke with musicOnly true filters only music libraries`() =
        runTest {
            val serverId = "server-123"
            val allLibraries =
                listOf(
                    TestFixtures.createPlexLibrary(id = "lib-1", title = "Music", type = LibraryType.MUSIC),
                    TestFixtures.createPlexLibrary(id = "lib-2", title = "Movies", type = LibraryType.MOVIES),
                    TestFixtures.createPlexLibrary(id = "lib-3", title = "TV Shows", type = LibraryType.TV_SHOWS),
                    TestFixtures.createPlexLibrary(id = "lib-4", title = "More Music", type = LibraryType.MUSIC),
                )
            coEvery { libraryRepository.refreshLibraries(serverId) } returns Result.success(allLibraries)

            useCase(serverId, musicOnly = true).test {
                val result = awaitItem()
                assertEquals(2, result.size)
                assertEquals("Music", result[0].title)
                assertEquals("More Music", result[1].title)
                awaitComplete()
            }
        }

    @Test
    fun `invoke with musicOnly false returns all libraries`() =
        runTest {
            val serverId = "server-123"
            val allLibraries =
                listOf(
                    TestFixtures.createPlexLibrary(id = "lib-1", title = "Music", type = LibraryType.MUSIC),
                    TestFixtures.createPlexLibrary(id = "lib-2", title = "Movies", type = LibraryType.MOVIES),
                    TestFixtures.createPlexLibrary(id = "lib-3", title = "TV Shows", type = LibraryType.TV_SHOWS),
                )
            coEvery { libraryRepository.refreshLibraries(serverId) } returns Result.success(allLibraries)

            useCase(serverId, musicOnly = false).test {
                val result = awaitItem()
                assertEquals(3, result.size)
                awaitComplete()
            }
        }

    @Test
    fun `invoke defaults to musicOnly true`() =
        runTest {
            val serverId = "server-123"
            val allLibraries =
                listOf(
                    TestFixtures.createPlexLibrary(id = "lib-1", title = "Music", type = LibraryType.MUSIC),
                    TestFixtures.createPlexLibrary(id = "lib-2", title = "Movies", type = LibraryType.MOVIES),
                )
            coEvery { libraryRepository.refreshLibraries(serverId) } returns Result.success(allLibraries)

            useCase(serverId).test {
                val result = awaitItem()
                assertEquals(1, result.size)
                assertEquals(LibraryType.MUSIC, result[0].type)
                awaitComplete()
            }
        }

    @Test
    fun `invoke returns empty list when no matching libraries`() =
        runTest {
            val serverId = "server-123"
            val allLibraries =
                listOf(
                    TestFixtures.createPlexLibrary(id = "lib-1", title = "Movies", type = LibraryType.MOVIES),
                )
            coEvery { libraryRepository.refreshLibraries(serverId) } returns Result.success(allLibraries)

            useCase(serverId, musicOnly = true).test {
                val result = awaitItem()
                assertEquals(0, result.size)
                awaitComplete()
            }
        }
}
