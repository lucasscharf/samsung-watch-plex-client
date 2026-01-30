package com.plexwatch.util

import com.plexwatch.domain.model.Album
import com.plexwatch.domain.model.Artist
import com.plexwatch.domain.model.LibraryType
import com.plexwatch.domain.model.PlexLibrary
import com.plexwatch.domain.model.PlexPin
import com.plexwatch.domain.model.PlexServer
import com.plexwatch.domain.model.PlexUser
import com.plexwatch.domain.model.Track

object TestFixtures {
    fun createPlexPin(
        id: Long = 12345L,
        code: String = "ABCD",
        expiresAt: Long = System.currentTimeMillis() + 300_000,
    ) = PlexPin(
        id = id,
        code = code,
        expiresAt = expiresAt,
    )

    fun createPlexUser(
        id: String = "user-123",
        username: String = "testuser",
        email: String = "test@example.com",
        thumb: String? = "https://plex.tv/thumb.jpg",
        authToken: String = "test-auth-token",
    ) = PlexUser(
        id = id,
        username = username,
        email = email,
        thumb = thumb,
        authToken = authToken,
    )

    fun createPlexServer(
        id: String = "server-123",
        name: String = "Test Server",
        address: String = "192.168.1.100",
        port: Int = 32400,
        isLocal: Boolean = true,
        isOwned: Boolean = true,
    ) = PlexServer(
        id = id,
        name = name,
        address = address,
        port = port,
        isLocal = isLocal,
        isOwned = isOwned,
    )

    fun createPlexLibrary(
        id: String = "library-123",
        key: String = "1",
        title: String = "Music Library",
        type: LibraryType = LibraryType.MUSIC,
        artUri: String? = "/library/art",
    ) = PlexLibrary(
        id = id,
        key = key,
        title = title,
        type = type,
        artUri = artUri,
    )

    fun createArtist(
        id: String = "artist-123",
        name: String = "Test Artist",
        thumbUri: String? = "/artist/thumb",
        albumCount: Int = 5,
    ) = Artist(
        id = id,
        name = name,
        thumbUri = thumbUri,
        albumCount = albumCount,
    )

    fun createAlbum(
        id: String = "album-123",
        title: String = "Test Album",
        artistName: String = "Test Artist",
        year: Int? = 2023,
        thumbUri: String? = "/album/thumb",
        trackCount: Int = 10,
    ) = Album(
        id = id,
        title = title,
        artistName = artistName,
        year = year,
        thumbUri = thumbUri,
        trackCount = trackCount,
    )

    fun createTrack(
        id: String = "track-123",
        title: String = "Test Track",
        artistName: String = "Test Artist",
        albumTitle: String = "Test Album",
        duration: Long = 180000L,
        trackNumber: Int = 1,
        thumbUri: String? = "/track/thumb",
        mediaKey: String = "/library/parts/123/file.mp3",
    ) = Track(
        id = id,
        title = title,
        artistName = artistName,
        albumTitle = albumTitle,
        duration = duration,
        trackNumber = trackNumber,
        thumbUri = thumbUri,
        mediaKey = mediaKey,
    )
}
