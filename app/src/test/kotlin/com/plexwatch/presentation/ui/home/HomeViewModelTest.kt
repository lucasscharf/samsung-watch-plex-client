package com.plexwatch.presentation.ui.home

import app.cash.turbine.test
import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.AuthRepository
import com.plexwatch.domain.repository.PlaybackRepository
import com.plexwatch.util.MainDispatcherRule
import com.plexwatch.util.TestFixtures
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
    private lateinit var playbackRepository: PlaybackRepository
    private val isAuthenticatedFlow = MutableStateFlow(false)
    private val currentTrackFlow = MutableStateFlow<Track?>(null)

    @Before
    fun setUp() {
        authRepository = mockk()
        playbackRepository = mockk()
        every { authRepository.isAuthenticated } returns isAuthenticatedFlow
        every { playbackRepository.currentTrack } returns currentTrackFlow
    }

    @Test
    fun `initial state is Loading`() =
        runTest {
            val viewModel = HomeViewModel(authRepository, playbackRepository)

            assertEquals(HomeUiState.Loading, viewModel.uiState.value)
        }

    @Test
    fun `emits NotAuthenticated when auth is false`() =
        runTest {
            isAuthenticatedFlow.value = false
            val viewModel = HomeViewModel(authRepository, playbackRepository)

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
            val viewModel = HomeViewModel(authRepository, playbackRepository)

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
            val viewModel = HomeViewModel(authRepository, playbackRepository)

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

    @Test
    fun `emits navigateToNowPlaying when track is playing on init`() =
        runTest {
            currentTrackFlow.value = TestFixtures.createTrack()
            val viewModel = HomeViewModel(authRepository, playbackRepository)

            viewModel.navigateToNowPlaying.test {
                advanceUntilIdle()
                assertEquals(Unit, awaitItem())
            }
        }

    @Test
    fun `does not emit navigateToNowPlaying when no track is playing`() =
        runTest {
            currentTrackFlow.value = null
            val viewModel = HomeViewModel(authRepository, playbackRepository)

            viewModel.navigateToNowPlaying.test {
                advanceUntilIdle()
                expectNoEvents()
            }
        }

    @Test
    fun `currentTrack exposes playback repository current track`() =
        runTest {
            val track = TestFixtures.createTrack(title = "Test Song")
            currentTrackFlow.value = track
            val viewModel = HomeViewModel(authRepository, playbackRepository)

            assertEquals(track, viewModel.currentTrack.value)
        }
}
