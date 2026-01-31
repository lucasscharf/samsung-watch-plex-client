package com.plexwatch.di

import com.plexwatch.data.local.TokenStorage
import com.plexwatch.data.local.TokenStorageInterface
import com.plexwatch.data.repository.AuthRepositoryImpl
import com.plexwatch.data.repository.LibraryRepositoryImpl
import com.plexwatch.data.repository.PlaybackRepositoryImpl
import com.plexwatch.data.repository.ServerRepositoryImpl
import com.plexwatch.domain.repository.AuthRepository
import com.plexwatch.domain.repository.LibraryRepository
import com.plexwatch.domain.repository.PlaybackRepository
import com.plexwatch.domain.repository.ServerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindServerRepository(impl: ServerRepositoryImpl): ServerRepository

    @Binds
    @Singleton
    abstract fun bindLibraryRepository(impl: LibraryRepositoryImpl): LibraryRepository

    @Binds
    @Singleton
    abstract fun bindTokenStorage(impl: TokenStorage): TokenStorageInterface

    @Binds
    @Singleton
    abstract fun bindPlaybackRepository(impl: PlaybackRepositoryImpl): PlaybackRepository
}
