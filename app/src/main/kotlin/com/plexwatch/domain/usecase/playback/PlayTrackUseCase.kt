package com.plexwatch.domain.usecase.playback

import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.PlaybackRepository
import javax.inject.Inject

class PlayTrackUseCase
    @Inject
    constructor(
        private val playbackRepository: PlaybackRepository,
    ) {
        suspend operator fun invoke(track: Track) {
            playbackRepository.play(track)
        }

        suspend operator fun invoke(
            tracks: List<Track>,
            startIndex: Int = 0,
        ) {
            playbackRepository.playQueue(tracks, startIndex)
        }
    }
