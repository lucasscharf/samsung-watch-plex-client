package com.plexwatch.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plexwatch.data.local.db.entity.LibrarySyncEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LibrarySyncDao {
    @Query("SELECT * FROM library_sync WHERE libraryKey = :libraryKey")
    fun getByLibraryKey(libraryKey: String): Flow<LibrarySyncEntity?>

    @Query("SELECT * FROM library_sync WHERE libraryKey = :libraryKey")
    suspend fun getByLibraryKeyOnce(libraryKey: String): LibrarySyncEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LibrarySyncEntity)

    @Query("DELETE FROM library_sync WHERE libraryKey = :libraryKey")
    suspend fun deleteByLibraryKey(libraryKey: String)

    @Query("DELETE FROM library_sync WHERE serverId = :serverId")
    suspend fun deleteByServerId(serverId: String)
}
