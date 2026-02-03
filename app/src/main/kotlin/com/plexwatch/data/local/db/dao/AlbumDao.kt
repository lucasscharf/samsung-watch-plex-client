package com.plexwatch.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plexwatch.data.local.db.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums WHERE artistId = :artistId ORDER BY year DESC, title ASC")
    fun getByArtistId(artistId: String): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM albums WHERE artistId = :artistId ORDER BY year DESC, title ASC")
    suspend fun getByArtistIdOnce(artistId: String): List<AlbumEntity>

    @Query("SELECT * FROM albums WHERE id = :albumId")
    suspend fun getById(albumId: String): AlbumEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<AlbumEntity>)

    @Query("DELETE FROM albums WHERE artistId = :artistId")
    suspend fun deleteByArtistId(artistId: String)
}
