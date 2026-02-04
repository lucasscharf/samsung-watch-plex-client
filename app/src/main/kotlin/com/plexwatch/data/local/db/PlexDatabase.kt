package com.plexwatch.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.plexwatch.data.local.db.dao.AlbumDao
import com.plexwatch.data.local.db.dao.ArtistDao
import com.plexwatch.data.local.db.dao.LibrarySyncDao
import com.plexwatch.data.local.db.dao.TrackDao
import com.plexwatch.data.local.db.entity.AlbumEntity
import com.plexwatch.data.local.db.entity.ArtistEntity
import com.plexwatch.data.local.db.entity.LibrarySyncEntity
import com.plexwatch.data.local.db.entity.TrackEntity

@Database(
    entities = [
        LibrarySyncEntity::class,
        ArtistEntity::class,
        AlbumEntity::class,
        TrackEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class PlexDatabase : RoomDatabase() {
    abstract fun librarySyncDao(): LibrarySyncDao

    abstract fun artistDao(): ArtistDao

    abstract fun albumDao(): AlbumDao

    abstract fun trackDao(): TrackDao
}
