package com.plexwatch.presentation.ui.home

import app.cash.turbine.test
import com.plexwatch.domain.repository.AuthRepository
import com.plexwatch.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var authRepository: AuthRepository
    private val isAuthenticatedFlow = MutableStateFlow(false)

    @Before
    fun setUp() {
        authRepository = mockk()
        every { authRepository.isAuthenticated } returns isAuthenticatedFlow
    }

    @Test
    fun `initial state is Loading`() =
        runTest {
            val viewModel = HomeViewModel(authRepository)

            assertEquals(HomeUiState.Loading, viewModel.uiState.value)
        }

    @Test
    fun `emits NotAuthenticated when auth is false`() =
        runTest {
            isAuthenticatedFlow.value = false
            val viewModel = HomeViewModel(authRepository)

            viewModel.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())
                advanceUntilIdle()
                assertEquals(HomeUiState.NotAuthenticated, awaitItem())
            }
        }

    @Test
    fun `emits Authenticated when auth is true`() =
        runTest {
            isAuthenticatedFlow.value = true
            val viewModel = HomeViewModel(authRepository)

            viewModel.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())
                advanceUntilIdle()
                assertEquals(HomeUiState.Authenticated, awaitItem())
            }
        }

    @Test
    fun `state changes when auth status changes`() =
        runTest {
            isAuthenticatedFlow.value = false
            val viewModel = HomeViewModel(authRepository)

            viewModel.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())
                advanceUntilIdle()
                assertEquals(HomeUiState.NotAuthenticated, awaitItem())

                isAuthenticatedFlow.value = true
                assertEquals(HomeUiState.Authenticated, awaitItem())

                isAuthenticatedFlow.value = false
                assertEquals(HomeUiState.NotAuthenticated, awaitItem())
            }
        }
}
