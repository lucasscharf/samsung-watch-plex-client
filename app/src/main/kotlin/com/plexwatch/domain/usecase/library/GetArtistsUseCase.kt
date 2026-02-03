package com.plexwatch.domain.usecase.library

import com.plexwatch.domain.model.Artist
import com.plexwatch.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArtistsUseCase
    @Inject
    constructor(
        private val libraryRepository: LibraryRepository,
    ) {
        operator fun invoke(libraryKey: String): Flow<List<Artist>> {
            return libraryRepository.getArtists(libraryKey)
        }
    }
