package com.plexwatch.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LibrarySectionsResponse(
    @Json(name = "MediaContainer") val mediaContainer: LibrarySectionsContainer,
)

@JsonClass(generateAdapter = true)
data class LibrarySectionsContainer(
    @Json(name = "Directory") val directories: List<DirectoryDto>?,
)

@JsonClass(generateAdapter = true)
data class DirectoryDto(
    @Json(name = "key") val key: String,
    @Json(name = "title") val title: String,
    @Json(name = "type") val type: String,
    @Json(name = "uuid") val uuid: String?,
    @Json(name = "art") val art: String?,
    @Json(name = "thumb") val thumb: String?,
)

@JsonClass(generateAdapter = true)
data class MediaContainerResponse(
    @Json(name = "MediaContainer") val mediaContainer: MediaContainer,
)

@JsonClass(generateAdapter = true)
data class MediaContainer(
    @Json(name = "size") val size: Int?,
    @Json(name = "Metadata") val metadata: List<MetadataDto>?,
)

@JsonClass(generateAdapter = true)
data class MetadataDto(
    @Json(name = "ratingKey") val ratingKey: String,
    @Json(name = "key") val key: String,
    @Json(name = "title") val title: String,
    @Json(name = "type") val type: String,
    @Json(name = "thumb") val thumb: String?,
    @Json(name = "art") val art: String?,
    @Json(name = "parentTitle") val parentTitle: String?,
    @Json(name = "grandparentTitle") val grandparentTitle: String?,
    @Json(name = "year") val year: Int?,
    @Json(name = "duration") val duration: Long?,
    @Json(name = "index") val index: Int?,
    @Json(name = "childCount") val childCount: Int?,
    @Json(name = "leafCount") val leafCount: Int?,
    @Json(name = "Media") val media: List<MediaInfoDto>?,
)

@JsonClass(generateAdapter = true)
data class MediaInfoDto(
    @Json(name = "id") val id: Long,
    @Json(name = "Part") val parts: List<PartDto>?,
)

@JsonClass(generateAdapter = true)
data class PartDto(
    @Json(name = "id") val id: Long,
    @Json(name = "key") val key: String,
    @Json(name = "file") val file: String?,
)
