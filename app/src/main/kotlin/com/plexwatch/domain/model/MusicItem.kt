package com.plexwatch.domain.model

data class Artist(
    val id: String,
    val name: String,
    val thumbUri: String?,
    val albumCount: Int,
)

data class Album(
    val id: String,
    val title: String,
    val artistName: String,
    val year: Int?,
    val thumbUri: String?,
    val trackCount: Int,
)

data class Track(
    val id: String,
    val title: String,
    val artistName: String,
    val albumTitle: String,
    val duration: Long,
    val trackNumber: Int,
    val thumbUri: String?,
    val mediaKey: String,
    val playCount: Int = 0,
)
