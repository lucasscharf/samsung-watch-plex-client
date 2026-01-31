package com.plexwatch.domain.usecase.library

import com.plexwatch.domain.model.Artist
import com.plexwatch.domain.repository.LibraryRepository
import javax.inject.Inject

class GetArtistsUseCase
    @Inject
    constructor(
        private val libraryRepository: LibraryRepository,
    ) {
        suspend operator fun invoke(libraryKey: String): Result<List<Artist>> {
            return libraryRepository.getArtists(libraryKey)
        }
    }
