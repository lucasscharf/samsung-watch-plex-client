package com.plexwatch.di

import android.content.Context
import androidx.room.Room
import com.plexwatch.data.local.db.PlexDatabase
import com.plexwatch.data.local.db.dao.AlbumDao
import com.plexwatch.data.local.db.dao.ArtistDao
import com.plexwatch.data.local.db.dao.LibrarySyncDao
import com.plexwatch.data.local.db.dao.TrackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePlexDatabase(
        @ApplicationContext context: Context,
    ): PlexDatabase =
        Room
            .databaseBuilder(
                context,
                PlexDatabase::class.java,
                "plex_database",
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideLibrarySyncDao(database: PlexDatabase): LibrarySyncDao = database.librarySyncDao()

    @Provides
    fun provideArtistDao(database: PlexDatabase): ArtistDao = database.artistDao()

    @Provides
    fun provideAlbumDao(database: PlexDatabase): AlbumDao = database.albumDao()

    @Provides
    fun provideTrackDao(database: PlexDatabase): TrackDao = database.trackDao()
}
