package com.plexwatch.domain.usecase.library

import com.plexwatch.domain.model.Track
import com.plexwatch.domain.repository.LibraryRepository
import javax.inject.Inject

class GetTracksUseCase
    @Inject
    constructor(
        private val libraryRepository: LibraryRepository,
    ) {
        suspend operator fun invoke(albumId: String): Result<List<Track>> {
            return libraryRepository.getAlbumTracks(albumId)
        }
    }
