package com.plexwatch.presentation.ui.nowplaying

import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.PlaybackRepository
import com.plexwatch.util.MainDispatcherRule
import com.plexwatch.util.TestFixtures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NowPlayingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var playbackRepository: PlaybackRepository
    private lateinit var viewModel: NowPlayingViewModel

    private val currentTrackFlow = MutableStateFlow<Track?>(null)
    private val isPlayingFlow = MutableStateFlow(false)
    private val progressFlow = MutableStateFlow(0L)
    private val queueFlow = MutableStateFlow<List<Track>>(emptyList())

    @Before
    fun setUp() {
        playbackRepository = mockk()
        every { playbackRepository.currentTrack } returns currentTrackFlow
        every { playbackRepository.isPlaying } returns isPlayingFlow
        every { playbackRepository.progress } returns progressFlow
        every { playbackRepository.queue } returns queueFlow

        viewModel = NowPlayingViewModel(playbackRepository)
    }

    @Test
    fun `initial state is Loading`() {
        assertEquals(NowPlayingUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `emits Empty when no track is playing`() =
        runTest {
            // Trigger flow emission
            currentTrackFlow.value = null
            queueFlow.value = emptyList()
            isPlayingFlow.value = false
            progressFlow.value = 0L
            advanceUntilIdle()

            // Collect from the stateIn flow
            val state = viewModel.uiState.first { it != NowPlayingUiState.Loading }
            assertEquals(NowPlayingUiState.Empty, state)
        }

    @Test
    fun `emits Playing state with track info`() =
        runTest {
            val track =
                TestFixtures.createTrack(
                    title = "Test Song",
                    artistName = "Test Artist",
                    albumTitle = "Test Album",
                    duration = 180000L,
                )
            val queue = listOf(track)

            queueFlow.value = queue
            currentTrackFlow.value = track
            isPlayingFlow.value = true
            progressFlow.value = 60000L
            advanceUntilIdle()

            val state = viewModel.uiState.first { it is NowPlayingUiState.Playing }
            assertTrue(state is NowPlayingUiState.Playing)

            val playingState = state as NowPlayingUiState.Playing
            assertEquals("Test Song", playingState.trackTitle)
            assertEquals("Test Artist", playingState.artistName)
            assertEquals("Test Album", playingState.albumTitle)
            assertTrue(playingState.isPlaying)
            assertEquals(60000L, playingState.progress)
            assertEquals(180000L, playingState.duration)
        }

    @Test
    fun `onPlayPauseClick calls pause when playing`() =
        runTest {
            val track = TestFixtures.createTrack()
            queueFlow.value = listOf(track)
            currentTrackFlow.value = track
            isPlayingFlow.value = true
            advanceUntilIdle()

            // Wait for state to update
            viewModel.uiState.first { it is NowPlayingUiState.Playing }

            coEvery { playbackRepository.pause() } just runs

            viewModel.onPlayPauseClick()
            advanceUntilIdle()

            coVerify(exactly = 1) { playbackRepository.pause() }
        }

    @Test
    fun `onPlayPauseClick calls resume when paused`() =
        runTest {
            val track = TestFixtures.createTrack()
            queueFlow.value = listOf(track)
            currentTrackFlow.value = track
            isPlayingFlow.value = false
            advanceUntilIdle()

            // Wait for state to update
            viewModel.uiState.first { it is NowPlayingUiState.Playing }

            coEvery { playbackRepository.resume() } just runs

            viewModel.onPlayPauseClick()
            advanceUntilIdle()

            coVerify(exactly = 1) { playbackRepository.resume() }
        }

    @Test
    fun `onSkipNextClick calls skipToNext`() =
        runTest {
            coEvery { playbackRepository.skipToNext() } just runs

            viewModel.onSkipNextClick()
            advanceUntilIdle()

            coVerify(exactly = 1) { playbackRepository.skipToNext() }
        }

    @Test
    fun `onSkipPreviousClick calls skipToPrevious`() =
        runTest {
            coEvery { playbackRepository.skipToPrevious() } just runs

            viewModel.onSkipPreviousClick()
            advanceUntilIdle()

            coVerify(exactly = 1) { playbackRepository.skipToPrevious() }
        }

    @Test
    fun `onSeekTo calls seekTo with position`() =
        runTest {
            val position = 90000L
            coEvery { playbackRepository.seekTo(position) } just runs

            viewModel.onSeekTo(position)
            advanceUntilIdle()

            coVerify(exactly = 1) { playbackRepository.seekTo(position) }
        }

    @Test
    fun `hasNext is true when more tracks in queue`() =
        runTest {
            val tracks =
                listOf(
                    TestFixtures.createTrack(id = "1", title = "Track 1"),
                    TestFixtures.createTrack(id = "2", title = "Track 2"),
                    TestFixtures.createTrack(id = "3", title = "Track 3"),
                )
            queueFlow.value = tracks
            currentTrackFlow.value = tracks[0]
            advanceUntilIdle()

            val state = viewModel.uiState.first { it is NowPlayingUiState.Playing }
            val playingState = state as NowPlayingUiState.Playing
            assertTrue(playingState.hasNext)
            assertFalse(playingState.hasPrevious)
        }

    @Test
    fun `hasPrevious is true when not first track`() =
        runTest {
            val tracks =
                listOf(
                    TestFixtures.createTrack(id = "1", title = "Track 1"),
                    TestFixtures.createTrack(id = "2", title = "Track 2"),
                    TestFixtures.createTrack(id = "3", title = "Track 3"),
                )
            queueFlow.value = tracks
            currentTrackFlow.value = tracks[1]
            advanceUntilIdle()

            val state = viewModel.uiState.first { it is NowPlayingUiState.Playing }
            val playingState = state as NowPlayingUiState.Playing
            assertTrue(playingState.hasNext)
            assertTrue(playingState.hasPrevious)
        }

    @Test
    fun `last track has no next`() =
        runTest {
            val tracks =
                listOf(
                    TestFixtures.createTrack(id = "1", title = "Track 1"),
                    TestFixtures.createTrack(id = "2", title = "Track 2"),
                )
            queueFlow.value = tracks
            currentTrackFlow.value = tracks[1]
            advanceUntilIdle()

            val state = viewModel.uiState.first { it is NowPlayingUiState.Playing }
            val playingState = state as NowPlayingUiState.Playing
            assertFalse(playingState.hasNext)
            assertTrue(playingState.hasPrevious)
        }
}
