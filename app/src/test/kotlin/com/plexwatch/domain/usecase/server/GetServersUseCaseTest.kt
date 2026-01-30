package com.plexwatch.domain.usecase.server

import app.cash.turbine.test
import com.plexwatch.domain.repository.ServerRepository
import com.plexwatch.util.TestFixtures
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetServersUseCaseTest {
    private lateinit var serverRepository: ServerRepository
    private lateinit var useCase: GetServersUseCase

    @Before
    fun setUp() {
        serverRepository = mockk()
        useCase = GetServersUseCase(serverRepository)
    }

    @Test
    fun `invoke returns flow from repository`() =
        runTest {
            val expectedServers =
                listOf(
                    TestFixtures.createPlexServer(id = "server-1", name = "Server 1"),
                    TestFixtures.createPlexServer(id = "server-2", name = "Server 2"),
                )
            every { serverRepository.getServers() } returns flowOf(expectedServers)

            useCase().test {
                assertEquals(expectedServers, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `invoke emits empty list when no servers`() =
        runTest {
            every { serverRepository.getServers() } returns flowOf(emptyList())

            useCase().test {
                assertEquals(emptyList<Any>(), awaitItem())
                awaitComplete()
            }
        }
}
