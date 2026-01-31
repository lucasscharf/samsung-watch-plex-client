package com.plexwatch.data.repository

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.plexwatch.data.service.PlaybackService
import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.LibraryRepository
import com.plexwatch.domain.repository.PlaybackRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val libraryRepository: LibraryRepository,
    ) : PlaybackRepository {
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        private var controllerFuture: ListenableFuture<MediaController>? = null
        private var controller: MediaController? = null

        private val _currentTrack = MutableStateFlow<Track?>(null)
        override val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

        private val _isPlaying = MutableStateFlow(false)
        override val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

        private val _progress = MutableStateFlow(0L)
        override val progress: StateFlow<Long> = _progress.asStateFlow()

        private val _queue = MutableStateFlow<List<Track>>(emptyList())
        override val queue: StateFlow<List<Track>> = _queue.asStateFlow()

        private val _currentIndex = MutableStateFlow(0)

        private val playerListener =
            object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    _isPlaying.value = playing
                }

                override fun onMediaItemTransition(
                    mediaItem: MediaItem?,
                    reason: Int,
                ) {
                    controller?.let { ctrl ->
                        _currentIndex.value = ctrl.currentMediaItemIndex
                        updateCurrentTrack()
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        _isPlaying.value = false
                    }
                }
            }

        init {
            initializeController()
            startProgressUpdates()
        }

        private fun initializeController() {
            val sessionToken =
                SessionToken(
                    context,
                    ComponentName(context, PlaybackService::class.java),
                )
            controllerFuture =
                MediaController
                    .Builder(context, sessionToken)
                    .buildAsync()

            controllerFuture?.addListener(
                {
                    controller = controllerFuture?.get()
                    controller?.addListener(playerListener)
                },
                MoreExecutors.directExecutor(),
            )
        }

        private fun startProgressUpdates() {
            scope.launch {
                while (true) {
                    delay(1000L)
                    controller?.let { ctrl ->
                        if (ctrl.isPlaying) {
                            _progress.value = ctrl.currentPosition
                        }
                    }
                }
            }
        }

        private fun updateCurrentTrack() {
            val index = _currentIndex.value
            val currentQueue = _queue.value
            _currentTrack.value = currentQueue.getOrNull(index)
        }

        override suspend fun play(track: Track) {
            playQueue(listOf(track), 0)
        }

        override suspend fun playQueue(
            tracks: List<Track>,
            startIndex: Int,
        ) {
            _queue.value = tracks
            _currentIndex.value = startIndex

            controller?.let { ctrl ->
                ctrl.clearMediaItems()

                val mediaItems =
                    tracks.map { track ->
                        buildMediaItem(track)
                    }

                ctrl.setMediaItems(mediaItems, startIndex, 0L)
                ctrl.prepare()
                ctrl.play()
            }

            updateCurrentTrack()
        }

        private suspend fun buildMediaItem(track: Track): MediaItem {
            val streamUrl = libraryRepository.getStreamUrl(track)

            val metadata =
                MediaMetadata
                    .Builder()
                    .setTitle(track.title)
                    .setArtist(track.artistName)
                    .setAlbumTitle(track.albumTitle)
                    .build()

            return MediaItem
                .Builder()
                .setUri(streamUrl)
                .setMediaMetadata(metadata)
                .setMediaId(track.id)
                .build()
        }

        override suspend fun pause() {
            controller?.pause()
        }

        override suspend fun resume() {
            controller?.play()
        }

        override suspend fun stop() {
            controller?.stop()
            _currentTrack.value = null
            _isPlaying.value = false
            _progress.value = 0L
        }

        override suspend fun seekTo(position: Long) {
            controller?.seekTo(position)
            _progress.value = position
        }

        override suspend fun skipToNext() {
            controller?.let { ctrl ->
                if (ctrl.hasNextMediaItem()) {
                    ctrl.seekToNextMediaItem()
                }
            }
        }

        override suspend fun skipToPrevious() {
            controller?.let { ctrl ->
                if (ctrl.currentPosition > 3000L) {
                    ctrl.seekTo(0L)
                } else if (ctrl.hasPreviousMediaItem()) {
                    ctrl.seekToPreviousMediaItem()
                } else {
                    ctrl.seekTo(0L)
                }
            }
        }

        override suspend fun setQueue(tracks: List<Track>) {
            _queue.value = tracks
        }
    }
