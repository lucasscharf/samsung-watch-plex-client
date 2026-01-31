package com.plexwatch.domain.usecase.library

import com.plexwatch.domain.model.LibraryType
import com.plexwatch.domain.model.PlexLibrary
import com.plexwatch.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLibrariesUseCase
    @Inject
    constructor(
        private val libraryRepository: LibraryRepository,
    ) {
        operator fun invoke(
            serverId: String,
            musicOnly: Boolean = true,
        ): Flow<List<PlexLibrary>> =
            flow {
                val result = libraryRepository.refreshLibraries(serverId)
                val libraries =
                    result.getOrElse {
                        throw it
                    }
                val filtered =
                    if (musicOnly) {
                        libraries.filter { it.type == LibraryType.MUSIC }
                    } else {
                        libraries
                    }
                emit(filtered)
            }
    }
