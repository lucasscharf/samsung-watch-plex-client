package com.plexwatch.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracks",
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("albumId")],
)
data class TrackEntity(
    @PrimaryKey
    val id: String,
    val albumId: String,
    val title: String,
    val artistName: String,
    val albumTitle: String,
    val duration: Long,
    val trackNumber: Int,
    val thumbUri: String?,
    val mediaKey: String,
    val playCount: Int = 0,
)
