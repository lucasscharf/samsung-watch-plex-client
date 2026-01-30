package com.plexwatch.domain.model

data class PlexLibrary(
    val id: String,
    val key: String,
    val title: String,
    val type: LibraryType,
    val artUri: String?,
)

enum class LibraryType {
    MUSIC,
    MOVIES,
    TV_SHOWS,
    PHOTOS,
    OTHER,
    ;

    companion object {
        fun fromString(type: String): LibraryType =
            when (type.lowercase()) {
                "artist" -> MUSIC
                "movie" -> MOVIES
                "show" -> TV_SHOWS
                "photo" -> PHOTOS
                else -> OTHER
            }
    }
}
