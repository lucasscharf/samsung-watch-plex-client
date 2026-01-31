package com.plexwatch.presentation.ui.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexwatch.domain.repository.PlaybackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel
    @Inject
    constructor(
        private val playbackRepository: PlaybackRepository,
    ) : ViewModel() {
        val uiState =
            combine(
                playbackRepository.currentTrack,
                playbackRepository.isPlaying,
                playbackRepository.progress,
                playbackRepository.queue,
            ) { track, playing, progress, queue ->
                if (track == null) {
                    NowPlayingUiState.Empty
                } else {
                    val currentIndex = queue.indexOf(track)
                    NowPlayingUiState.Playing(
                        trackTitle = track.title,
                        artistName = track.artistName,
                        albumTitle = track.albumTitle,
                        thumbUri = track.thumbUri,
                        isPlaying = playing,
                        progress = progress,
                        duration = track.duration,
                        hasNext = currentIndex < queue.size - 1,
                        hasPrevious = currentIndex > 0,
                    )
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = NowPlayingUiState.Loading,
            )

        fun onPlayPauseClick() {
            viewModelScope.launch {
                val currentState = uiState.value
                if (currentState is NowPlayingUiState.Playing) {
                    if (currentState.isPlaying) {
                        playbackRepository.pause()
                    } else {
                        playbackRepository.resume()
                    }
                }
            }
        }

        fun onSkipNextClick() {
            viewModelScope.launch {
                playbackRepository.skipToNext()
            }
        }

        fun onSkipPreviousClick() {
            viewModelScope.launch {
                playbackRepository.skipToPrevious()
            }
        }

        fun onSeekTo(position: Long) {
            viewModelScope.launch {
                playbackRepository.seekTo(position)
            }
        }
    }
