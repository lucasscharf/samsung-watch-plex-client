package com.plexwatch.domain.usecase.auth

import com.plexwatch.domain.repository.AuthRepository
import com.plexwatch.util.TestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreatePinUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: CreatePinUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        useCase = CreatePinUseCase(authRepository)
    }

    @Test
    fun `invoke returns pin on success`() =
        runTest {
            val expectedPin = TestFixtures.createPlexPin()
            coEvery { authRepository.createPin() } returns Result.success(expectedPin)

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals(expectedPin, result.getOrNull())
        }

    @Test
    fun `invoke propagates error on failure`() =
        runTest {
            val expectedException = RuntimeException("Network error")
            coEvery { authRepository.createPin() } returns Result.failure(expectedException)

            val result = useCase()

            assertTrue(result.isFailure)
            assertEquals(expectedException, result.exceptionOrNull())
        }
}
