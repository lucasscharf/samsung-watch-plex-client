package com.plexwatch.data.local.db

import com.plexwatch.data.local.db.entity.AlbumEntity
import com.plexwatch.data.local.db.entity.ArtistEntity
import com.plexwatch.data.local.db.entity.TrackEntity
import com.plexwatch.domain.model.Album
import com.plexwatch.domain.model.Artist
import com.plexwatch.domain.model.Track

fun ArtistEntity.toDomain(): Artist =
    Artist(
        id = id,
        name = name,
        thumbUri = thumbUri,
        albumCount = albumCount,
    )

fun Artist.toEntity(libraryKey: String): ArtistEntity =
    ArtistEntity(
        id = id,
        libraryKey = libraryKey,
        name = name,
        thumbUri = thumbUri,
        albumCount = albumCount,
    )

fun AlbumEntity.toDomain(): Album =
    Album(
        id = id,
        title = title,
        artistName = artistName,
        year = year,
        thumbUri = thumbUri,
        trackCount = trackCount,
    )

fun Album.toEntity(artistId: String): AlbumEntity =
    AlbumEntity(
        id = id,
        artistId = artistId,
        title = title,
        artistName = artistName,
        year = year,
        thumbUri = thumbUri,
        trackCount = trackCount,
    )

fun TrackEntity.toDomain(): Track =
    Track(
        id = id,
        title = title,
        artistName = artistName,
        albumTitle = albumTitle,
        duration = duration,
        trackNumber = trackNumber,
        thumbUri = thumbUri,
        mediaKey = mediaKey,
        playCount = playCount,
    )

fun Track.toEntity(albumId: String): TrackEntity =
    TrackEntity(
        id = id,
        albumId = albumId,
        title = title,
        artistName = artistName,
        albumTitle = albumTitle,
        duration = duration,
        trackNumber = trackNumber,
        thumbUri = thumbUri,
        mediaKey = mediaKey,
    )
