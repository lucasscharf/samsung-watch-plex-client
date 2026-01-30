package com.plexwatch.domain.usecase.playback

import com.plexwatch.domain.repository.PlaybackRepository
import com.plexwatch.util.TestFixtures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PlayTrackUseCaseTest {
    private lateinit var playbackRepository: PlaybackRepository
    private lateinit var useCase: PlayTrackUseCase

    @Before
    fun setUp() {
        playbackRepository = mockk()
        useCase = PlayTrackUseCase(playbackRepository)
    }

    @Test
    fun `invoke with single track calls play`() =
        runTest {
            val track = TestFixtures.createTrack()
            coEvery { playbackRepository.play(track) } just runs

            useCase(track)

            coVerify(exactly = 1) { playbackRepository.play(track) }
            coVerify(exactly = 0) { playbackRepository.playQueue(any(), any()) }
        }

    @Test
    fun `invoke with track list calls playQueue`() =
        runTest {
            val tracks =
                listOf(
                    TestFixtures.createTrack(id = "track-1", title = "Track 1"),
                    TestFixtures.createTrack(id = "track-2", title = "Track 2"),
                    TestFixtures.createTrack(id = "track-3", title = "Track 3"),
                )
            coEvery { playbackRepository.playQueue(tracks, 0) } just runs

            useCase(tracks)

            coVerify(exactly = 1) { playbackRepository.playQueue(tracks, 0) }
            coVerify(exactly = 0) { playbackRepository.play(any()) }
        }

    @Test
    fun `invoke with track list and start index calls playQueue with index`() =
        runTest {
            val tracks =
                listOf(
                    TestFixtures.createTrack(id = "track-1", title = "Track 1"),
                    TestFixtures.createTrack(id = "track-2", title = "Track 2"),
                    TestFixtures.createTrack(id = "track-3", title = "Track 3"),
                )
            val startIndex = 2
            coEvery { playbackRepository.playQueue(tracks, startIndex) } just runs

            useCase(tracks, startIndex)

            coVerify(exactly = 1) { playbackRepository.playQueue(tracks, startIndex) }
        }
}
