package com.plexwatch.presentation.ui.login

import app.cash.turbine.test
import com.plexwatch.domain.model.PlexPin
import com.plexwatch.domain.usecase.auth.CheckPinUseCase
import com.plexwatch.domain.usecase.auth.CreatePinUseCase
import com.plexwatch.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var createPinUseCase: CreatePinUseCase
    private lateinit var checkPinUseCase: CheckPinUseCase

    @Before
    fun setUp() {
        createPinUseCase = mockk()
        checkPinUseCase = mockk()
    }

    @Test
    fun `initial state is Loading`() =
        runTest {
            val pin = createFuturePin(300)
            coEvery { createPinUseCase() } returns Result.success(pin)
            coEvery { checkPinUseCase(pin.id) } returns Result.success(null)

            val viewModel = LoginViewModel(createPinUseCase, checkPinUseCase)

            assertEquals(LoginUiState.Loading, viewModel.uiState.value)
        }

    @Test
    fun `transitions to ShowingPin after pin created`() =
        runTest {
            val pin = createFuturePin(300)
            coEvery { createPinUseCase() } returns Result.success(pin)
            coEvery { checkPinUseCase(pin.id) } returns Result.success(null)

            val viewModel = LoginViewModel(createPinUseCase, checkPinUseCase)

            viewModel.uiState.test {
                skipItems(1) // Skip Loading
                advanceUntilIdle()
                val state = awaitItem()
                assertTrue("Expected ShowingPin but got $state", state is LoginUiState.ShowingPin)
                assertEquals(pin, (state as LoginUiState.ShowingPin).pin)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `error state when createPin fails`() =
        runTest {
            coEvery { createPinUseCase() } returns Result.failure(RuntimeException("Network error"))

            val viewModel = LoginViewModel(createPinUseCase, checkPinUseCase)

            viewModel.uiState.test {
                skipItems(1) // Skip Loading
                advanceUntilIdle()
                val state = awaitItem()
                assertTrue("Expected Error but got $state", state is LoginUiState.Error)
                assertEquals("Network error", (state as LoginUiState.Error).message)
            }
        }

    @Test
    fun `retry restarts login flow from Loading`() =
        runTest {
            val firstPin = createFuturePin(300, id = 1L, code = "AAAA")
            val secondPin = createFuturePin(300, id = 2L, code = "BBBB")

            coEvery { createPinUseCase() } returns Result.success(firstPin) andThen Result.success(secondPin)
            coEvery { checkPinUseCase(any()) } returns Result.success(null)

            val viewModel = LoginViewModel(createPinUseCase, checkPinUseCase)

            viewModel.uiState.test {
                skipItems(1) // Skip Loading
                advanceUntilIdle()
                val firstState = awaitItem() as LoginUiState.ShowingPin
                assertEquals("AAAA", firstState.pin.code)
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.retry()

            viewModel.uiState.test {
                // After retry, should transition to Loading then to ShowingPin
                val currentState = awaitItem()
                if (currentState is LoginUiState.Loading) {
                    advanceUntilIdle()
                    val state = awaitItem()
                    assertTrue("Expected ShowingPin but got $state", state is LoginUiState.ShowingPin)
                    assertEquals("BBBB", (state as LoginUiState.ShowingPin).pin.code)
                } else if (currentState is LoginUiState.ShowingPin) {
                    assertEquals("BBBB", currentState.pin.code)
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `showing pin state has remaining seconds`() =
        runTest {
            val pin = createFuturePin(300)
            coEvery { createPinUseCase() } returns Result.success(pin)
            coEvery { checkPinUseCase(pin.id) } returns Result.success(null)

            val viewModel = LoginViewModel(createPinUseCase, checkPinUseCase)

            viewModel.uiState.test {
                skipItems(1) // Skip Loading
                advanceUntilIdle()
                val state = awaitItem() as LoginUiState.ShowingPin
                assertTrue(state.remainingSeconds > 0)
                assertTrue(state.remainingSeconds <= 300)
                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun createFuturePin(
        secondsUntilExpiry: Int,
        id: Long = 12345L,
        code: String = "ABCD",
    ): PlexPin =
        PlexPin(
            id = id,
            code = code,
            expiresAt = System.currentTimeMillis() + (secondsUntilExpiry * 1000L),
        )
}
