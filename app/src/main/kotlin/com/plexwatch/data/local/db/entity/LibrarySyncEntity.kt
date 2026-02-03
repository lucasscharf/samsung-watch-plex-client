package com.plexwatch.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_sync")
data class LibrarySyncEntity(
    @PrimaryKey
    val libraryKey: String,
    val serverId: String,
    val lastSyncedAt: Long,
    val artistCount: Int,
)
