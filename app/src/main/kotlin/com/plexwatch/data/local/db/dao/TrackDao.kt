package com.plexwatch.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plexwatch.data.local.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks WHERE albumId = :albumId ORDER BY trackNumber ASC")
    fun getByAlbumId(albumId: String): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE albumId = :albumId ORDER BY trackNumber ASC")
    suspend fun getByAlbumIdOnce(albumId: String): List<TrackEntity>

    @Query("SELECT * FROM tracks WHERE id = :trackId")
    suspend fun getById(trackId: String): TrackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<TrackEntity>)

    @Query("DELETE FROM tracks WHERE albumId = :albumId")
    suspend fun deleteByAlbumId(albumId: String)
}
