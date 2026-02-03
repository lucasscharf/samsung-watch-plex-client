package com.plexwatch.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "albums",
    foreignKeys = [
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("artistId")],
)
data class AlbumEntity(
    @PrimaryKey
    val id: String,
    val artistId: String,
    val title: String,
    val artistName: String,
    val year: Int?,
    val thumbUri: String?,
    val trackCount: Int,
)
