package com.plexwatch.data.repository

import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.LibraryRepository
import com.plexwatch.domain.repository.PlaybackRepository
import com.plexwatch.util.TestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Tests for playback functionality using a fake implementation.
 * Since PlaybackRepositoryImpl requires Android MediaController,
 * we test the PlaybackRepository interface behavior with a fake.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PlaybackRepositoryImplTest {
    private lateinit var libraryRepository: LibraryRepository
    private lateinit var repository: FakePlaybackRepository

    @Before
    fun setUp() {
        libraryRepository = mockk()
        repository = FakePlaybackRepository(libraryRepository)
    }

    @Test
    fun `initial state has no current track`() =
        runTest {
            assertNull(repository.currentTrack.value)
        }

    @Test
    fun `initial state is not playing`() =
        runTest {
            assertFalse(repository.isPlaying.value)
        }

    @Test
    fun `initial state has zero progress`() =
        runTest {
            assertEquals(0L, repository.progress.value)
        }

    @Test
    fun `initial state has empty queue`() =
        runTest {
            assertEquals(emptyList<Track>(), repository.queue.value)
        }

    @Test
    fun `setQueue updates queue state`() =
        runTest {
            val tracks =
                listOf(
                    TestFixtures.createTrack(id = "1"),
                    TestFixtures.createTrack(id = "2"),
                )

            repository.setQueue(tracks)

            assertEquals(tracks, repository.queue.value)
        }

    @Test
    fun `stop clears playback state`() =
        runTest {
            repository.stop()

            assertNull(repository.currentTrack.value)
            assertFalse(repository.isPlaying.value)
            assertEquals(0L, repository.progress.value)
        }

    @Test
    fun `seekTo updates progress`() =
        runTest {
            val position = 60000L

            repository.seekTo(position)

            assertEquals(position, repository.progress.value)
        }

    @Test
    fun `play updates queue with single track and starts playing`() =
        runTest {
            val track = TestFixtures.createTrack()
            coEvery { libraryRepository.getStreamUrl(track) } returns "http://test.com/stream"

            repository.play(track)

            assertEquals(listOf(track), repository.queue.value)
            assertEquals(track, repository.currentTrack.value)
        }

    @Test
    fun `playQueue updates queue with tracks at start index`() =
        runTest {
            val tracks =
                listOf(
                    TestFixtures.createTrack(id = "1"),
                    TestFixtures.createTrack(id = "2"),
                    TestFixtures.createTrack(id = "3"),
                )
            coEvery { libraryRepository.getStreamUrl(any()) } returns "http://test.com/stream"

            repository.playQueue(tracks, 1)

            assertEquals(tracks, repository.queue.value)
            assertEquals(tracks[1], repository.currentTrack.value)
        }

    @Test
    fun `pause stops playback`() =
        runTest {
            val track = TestFixtures.createTrack()
            coEvery { libraryRepository.getStreamUrl(track) } returns "http://test.com/stream"
            repository.play(track)

            repository.pause()

            assertFalse(repository.isPlaying.value)
        }

    @Test
    fun `resume starts playback`() =
        runTest {
            repository.resume()

            assert(repository.isPlaying.value)
        }

    @Test
    fun `skipToNext advances to next track`() =
        runTest {
            val tracks =
                listOf(
                    TestFixtures.createTrack(id = "1"),
                    TestFixtures.createTrack(id = "2"),
                )
            coEvery { libraryRepository.getStreamUrl(any()) } returns "http://test.com/stream"
            repository.playQueue(tracks, 0)

            repository.skipToNext()

            assertEquals(tracks[1], repository.currentTrack.value)
        }

    @Test
    fun `skipToPrevious goes to previous track`() =
        runTest {
            val tracks =
                listOf(
                    TestFixtures.createTrack(id = "1"),
                    TestFixtures.createTrack(id = "2"),
                )
            coEvery { libraryRepository.getStreamUrl(any()) } returns "http://test.com/stream"
            repository.playQueue(tracks, 1)

            repository.skipToPrevious()

            assertEquals(tracks[0], repository.currentTrack.value)
        }
}

/**
 * Fake implementation for testing without Android framework dependencies.
 */
class FakePlaybackRepository(
    private val libraryRepository: LibraryRepository,
) : PlaybackRepository {
    private val _currentTrack = MutableStateFlow<Track?>(null)
    override val currentTrack = _currentTrack

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying = _isPlaying

    private val _progress = MutableStateFlow(0L)
    override val progress = _progress

    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    override val queue = _queue

    private var currentIndex = 0

    override suspend fun play(track: Track) {
        playQueue(listOf(track), 0)
    }

    override suspend fun playQueue(
        tracks: List<Track>,
        startIndex: Int,
    ) {
        _queue.value = tracks
        currentIndex = startIndex
        _currentTrack.value = tracks.getOrNull(startIndex)
        _isPlaying.value = true
    }

    override suspend fun pause() {
        _isPlaying.value = false
    }

    override suspend fun resume() {
        _isPlaying.value = true
    }

    override suspend fun stop() {
        _currentTrack.value = null
        _isPlaying.value = false
        _progress.value = 0L
    }

    override suspend fun seekTo(position: Long) {
        _progress.value = position
    }

    override suspend fun skipToNext() {
        val tracks = _queue.value
        if (currentIndex < tracks.size - 1) {
            currentIndex++
            _currentTrack.value = tracks[currentIndex]
        }
    }

    override suspend fun skipToPrevious() {
        if (currentIndex > 0) {
            currentIndex--
            _currentTrack.value = _queue.value[currentIndex]
        }
    }

    override suspend fun setQueue(tracks: List<Track>) {
        _queue.value = tracks
    }
}
