package com.plexwatch.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plexwatch.data.local.db.entity.ArtistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists WHERE libraryKey = :libraryKey ORDER BY name ASC")
    fun getByLibraryKey(libraryKey: String): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE libraryKey = :libraryKey ORDER BY name ASC")
    suspend fun getByLibraryKeyOnce(libraryKey: String): List<ArtistEntity>

    @Query("SELECT * FROM artists WHERE id = :artistId")
    suspend fun getById(artistId: String): ArtistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ArtistEntity>)

    @Query("DELETE FROM artists WHERE libraryKey = :libraryKey")
    suspend fun deleteByLibraryKey(libraryKey: String)
}
