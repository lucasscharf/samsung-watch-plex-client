package com.plexwatch.domain.usecase.library

import com.plexwatch.domain.model.Album
import com.plexwatch.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsUseCase
    @Inject
    constructor(
        private val libraryRepository: LibraryRepository,
    ) {
        operator fun invoke(artistId: String): Flow<List<Album>> {
            return libraryRepository.getAlbums(artistId)
        }
    }
