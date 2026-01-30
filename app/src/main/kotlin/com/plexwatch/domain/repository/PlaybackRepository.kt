package com.plexwatch.domain.repository

import com.plexwatch.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface PlaybackRepository {
    val currentTrack: StateFlow<Track?>
    val isPlaying: StateFlow<Boolean>
    val progress: StateFlow<Long>
    val queue: StateFlow<List<Track>>

    suspend fun play(track: Track)

    suspend fun playQueue(
        tracks: List<Track>,
        startIndex: Int = 0,
    )

    suspend fun pause()

    suspend fun resume()

    suspend fun stop()

    suspend fun seekTo(position: Long)

    suspend fun skipToNext()

    suspend fun skipToPrevious()

    suspend fun setQueue(tracks: List<Track>)
}
