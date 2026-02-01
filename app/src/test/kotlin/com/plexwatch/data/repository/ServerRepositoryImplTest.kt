package com.plexwatch.data.repository

import app.cash.turbine.test
import com.plexwatch.data.api.PlexServerApi
import com.plexwatch.data.api.dto.ConnectionDto
import com.plexwatch.data.api.dto.ResourcesResponse
import com.plexwatch.data.local.FakeTokenStorage
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ServerRepositoryImplTest {
    private lateinit var serverApi: PlexServerApi
    private lateinit var tokenStorage: FakeTokenStorage
    private lateinit var repository: ServerRepositoryImpl

    @Before
    fun setUp() {
        serverApi = mockk()
        tokenStorage = FakeTokenStorage()
        repository = ServerRepositoryImpl(serverApi, tokenStorage)
    }

    @Test
    fun `getServers returns empty flow initially`() =
        runTest {
            repository.getServers().test {
                assertEquals(emptyList<Any>(), awaitItem())
            }
        }

    @Test
    fun `refreshServers maps resources to servers with relay connection`() =
        runTest {
            val resources =
                listOf(
                    ResourcesResponse(
                        name = "My Server",
                        clientIdentifier = "server-123",
                        provides = "server",
                        owned = true,
                        connections =
                            listOf(
                                ConnectionDto(
                                    protocol = "https",
                                    address = "45.79.210.125",
                                    port = 8443,
                                    uri = "https://45-79-210-125.abc123.plex.direct:8443",
                                    local = false,
                                    relay = true,
                                ),
                            ),
                    ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { serverApi.getResources(any(), any(), any()) } returns resources

            val result = repository.refreshServers()

            assertTrue(result.isSuccess)
            val servers = result.getOrNull()!!
            assertEquals(1, servers.size)
            assertEquals("My Server", servers[0].name)
            assertEquals("server-123", servers[0].id)
            assertEquals("https://45-79-210-125.abc123.plex.direct:8443", servers[0].baseUrl)
            assertTrue(servers[0].isOwned)
        }

    @Test
    fun `refreshServers filters by server type`() =
        runTest {
            val resources =
                listOf(
                    ResourcesResponse(
                        name = "Server",
                        clientIdentifier = "server-123",
                        provides = "server",
                        owned = true,
                        connections =
                            listOf(
                                ConnectionDto(
                                    protocol = "https",
                                    address = "45.79.210.125",
                                    port = 8443,
                                    uri = "https://45-79-210-125.abc123.plex.direct:8443",
                                    local = false,
                                    relay = true,
                                ),
                            ),
                    ),
                    ResourcesResponse(
                        name = "Player",
                        clientIdentifier = "player-123",
                        provides = "player",
                        owned = true,
                        connections =
                            listOf(
                                ConnectionDto(
                                    protocol = "https",
                                    address = "45.79.210.126",
                                    port = 8443,
                                    uri = "https://45-79-210-126.abc123.plex.direct:8443",
                                    local = false,
                                    relay = true,
                                ),
                            ),
                    ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { serverApi.getResources(any(), any(), any()) } returns resources

            val result = repository.refreshServers()

            assertTrue(result.isSuccess)
            val servers = result.getOrNull()!!
            assertEquals(1, servers.size)
            assertEquals("Server", servers[0].name)
        }

    @Test
    fun `refreshServers filters only relay connections`() =
        runTest {
            val resources =
                listOf(
                    ResourcesResponse(
                        name = "My Server",
                        clientIdentifier = "server-123",
                        provides = "server",
                        owned = true,
                        connections =
                            listOf(
                                ConnectionDto(
                                    protocol = "http",
                                    address = "192.168.1.100",
                                    port = 32400,
                                    uri = "http://192.168.1.100:32400",
                                    local = true,
                                    relay = false,
                                ),
                                ConnectionDto(
                                    protocol = "https",
                                    address = "45.79.210.125",
                                    port = 8443,
                                    uri = "https://45-79-210-125.abc123.plex.direct:8443",
                                    local = false,
                                    relay = true,
                                ),
                            ),
                    ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { serverApi.getResources(any(), any(), any()) } returns resources

            val result = repository.refreshServers()

            assertTrue(result.isSuccess)
            val servers = result.getOrNull()!!
            assertEquals(1, servers.size)
            assertEquals("https://45-79-210-125.abc123.plex.direct:8443", servers[0].baseUrl)
        }

    @Test
    fun `refreshServers ignores server without relay connection`() =
        runTest {
            val resources =
                listOf(
                    ResourcesResponse(
                        name = "My Server",
                        clientIdentifier = "server-123",
                        provides = "server",
                        owned = true,
                        connections =
                            listOf(
                                ConnectionDto(
                                    protocol = "http",
                                    address = "192.168.1.100",
                                    port = 32400,
                                    uri = "http://192.168.1.100:32400",
                                    local = true,
                                    relay = false,
                                ),
                                ConnectionDto(
                                    protocol = "https",
                                    address = "external.domain.com",
                                    port = 32400,
                                    uri = "https://external.domain.com:32400",
                                    local = false,
                                    relay = false,
                                ),
                            ),
                    ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { serverApi.getResources(any(), any(), any()) } returns resources

            val result = repository.refreshServers()

            assertTrue(result.isSuccess)
            val servers = result.getOrNull()!!
            assertEquals(0, servers.size)
        }

    @Test
    fun `refreshServers fails when not authenticated`() =
        runTest {
            tokenStorage.setAuthToken(null)

            val result = repository.refreshServers()

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is IllegalStateException)
        }

    @Test
    fun `refreshServers handles empty connections`() =
        runTest {
            val resources =
                listOf(
                    ResourcesResponse(
                        name = "Server",
                        clientIdentifier = "server-123",
                        provides = "server",
                        owned = true,
                        connections = null,
                    ),
                )
            tokenStorage.setAuthToken("test-token")
            coEvery { serverApi.getResources(any(), any(), any()) } returns resources

            val result = repository.refreshServers()

            assertTrue(result.isSuccess)
            val servers = result.getOrNull()!!
            assertEquals(0, servers.size)
        }

    @Test
    fun `selectServer updates selected server`() =
        runTest {
            val server = com.plexwatch.util.TestFixtures.createPlexServer()

            repository.selectServer(server)

            repository.getSelectedServer().test {
                assertEquals(server, awaitItem())
            }
        }
}
