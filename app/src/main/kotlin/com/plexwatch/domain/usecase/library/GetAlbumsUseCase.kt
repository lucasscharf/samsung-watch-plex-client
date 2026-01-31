package com.plexwatch.domain.usecase.library

import com.plexwatch.domain.model.Album
import com.plexwatch.domain.repository.LibraryRepository
import javax.inject.Inject

class GetAlbumsUseCase
    @Inject
    constructor(
        private val libraryRepository: LibraryRepository,
    ) {
        suspend operator fun invoke(artistId: String): Result<List<Album>> {
            return libraryRepository.getAlbums(artistId)
        }
    }
