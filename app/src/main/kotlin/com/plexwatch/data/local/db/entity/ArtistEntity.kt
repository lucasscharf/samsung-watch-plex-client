package com.plexwatch.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "artists",
    foreignKeys = [
        ForeignKey(
            entity = LibrarySyncEntity::class,
            parentColumns = ["libraryKey"],
            childColumns = ["libraryKey"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("libraryKey")],
)
data class ArtistEntity(
    @PrimaryKey
    val id: String,
    val libraryKey: String,
    val name: String,
    val thumbUri: String?,
    val albumCount: Int,
)
