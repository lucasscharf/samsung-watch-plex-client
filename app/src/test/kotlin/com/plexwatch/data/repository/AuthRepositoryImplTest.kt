package com.plexwatch.data.repository

import app.cash.turbine.test
import com.plexwatch.data.api.PlexAuthApi
import com.plexwatch.data.api.dto.PinResponse
import com.plexwatch.data.api.dto.UserResponse
import com.plexwatch.data.local.FakeTokenStorage
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {
    private lateinit var authApi: PlexAuthApi
    private lateinit var tokenStorage: FakeTokenStorage
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        authApi = mockk()
        tokenStorage = FakeTokenStorage()
        repository = AuthRepositoryImpl(authApi, tokenStorage)
    }

    @Test
    fun `isAuthenticated emits false when no token`() =
        runTest {
            tokenStorage.setAuthToken(null)

            repository.isAuthenticated.test {
                assertFalse(awaitItem())
            }
        }

    @Test
    fun `isAuthenticated emits true when token exists`() =
        runTest {
            tokenStorage.setAuthToken("test-token")

            repository.isAuthenticated.test {
                assertTrue(awaitItem())
            }
        }

    @Test
    fun `createPin returns pin on success`() =
        runTest {
            val pinResponse =
                PinResponse(
                    id = 12345L,
                    code = "ABCD",
                    expiresAt = null,
                    authToken = null,
                )
            coEvery { authApi.createPin(any(), any(), any()) } returns pinResponse

            val result = repository.createPin()

            assertTrue(result.isSuccess)
            val pin = result.getOrNull()
            assertNotNull(pin)
            assertEquals(12345L, pin?.id)
            assertEquals("ABCD", pin?.code)
        }

    @Test
    fun `createPin returns failure on error`() =
        runTest {
            coEvery { authApi.createPin(any(), any(), any()) } throws RuntimeException("Network error")

            val result = repository.createPin()

            assertTrue(result.isFailure)
        }

    @Test
    fun `checkPin returns null when not authorized`() =
        runTest {
            val pinResponse =
                PinResponse(
                    id = 12345L,
                    code = "ABCD",
                    expiresAt = null,
                    authToken = null,
                )
            coEvery { authApi.checkPin(12345L, any()) } returns pinResponse

            val result = repository.checkPin(12345L)

            assertTrue(result.isSuccess)
            assertNull(result.getOrNull())
        }

    @Test
    fun `checkPin saves token and returns user when authorized`() =
        runTest {
            val pinResponse =
                PinResponse(
                    id = 12345L,
                    code = "ABCD",
                    expiresAt = null,
                    authToken = "new-auth-token",
                )
            val userResponse =
                UserResponse(
                    id = 999L,
                    uuid = "user-uuid",
                    username = "testuser",
                    email = "test@example.com",
                    thumb = null,
                )
            coEvery { authApi.checkPin(12345L, any()) } returns pinResponse
            coEvery { authApi.getUser("new-auth-token", any()) } returns userResponse

            val result = repository.checkPin(12345L)

            assertTrue(result.isSuccess)
            val user = result.getOrNull()
            assertNotNull(user)
            assertEquals("testuser", user?.username)
            assertEquals("new-auth-token", user?.authToken)
            assertTrue(tokenStorage.setAuthTokenCalled)
            assertEquals("new-auth-token", tokenStorage.lastAuthToken)
            assertTrue(tokenStorage.setUserIdCalled)
            assertEquals("999", tokenStorage.lastUserId)
        }

    @Test
    fun `logout clears token storage`() =
        runTest {
            repository.logout()

            assertTrue(tokenStorage.clearCalled)
        }
}
