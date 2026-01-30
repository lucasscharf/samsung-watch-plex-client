package com.plexwatch.domain.usecase.auth

import com.plexwatch.domain.repository.AuthRepository
import com.plexwatch.util.TestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheckPinUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: CheckPinUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        useCase = CheckPinUseCase(authRepository)
    }

    @Test
    fun `invoke returns null when pin is not authorized`() =
        runTest {
            val pinId = 12345L
            coEvery { authRepository.checkPin(pinId) } returns Result.success(null)

            val result = useCase(pinId)

            assertTrue(result.isSuccess)
            assertNull(result.getOrNull())
        }

    @Test
    fun `invoke returns user when pin is authorized`() =
        runTest {
            val pinId = 12345L
            val expectedUser = TestFixtures.createPlexUser()
            coEvery { authRepository.checkPin(pinId) } returns Result.success(expectedUser)

            val result = useCase(pinId)

            assertTrue(result.isSuccess)
            assertEquals(expectedUser, result.getOrNull())
        }

    @Test
    fun `invoke propagates error on failure`() =
        runTest {
            val pinId = 12345L
            val expectedException = RuntimeException("Network error")
            coEvery { authRepository.checkPin(pinId) } returns Result.failure(expectedException)

            val result = useCase(pinId)

            assertTrue(result.isFailure)
            assertEquals(expectedException, result.exceptionOrNull())
        }
}
