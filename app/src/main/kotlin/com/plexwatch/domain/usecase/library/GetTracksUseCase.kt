package com.plexwatch.domain.usecase.library

import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTracksUseCase
    @Inject
    constructor(
        private val libraryRepository: LibraryRepository,
    ) {
        operator fun invoke(albumId: String): Flow<List<Track>> {
            return libraryRepository.getAlbumTracks(albumId)
        }
    }
