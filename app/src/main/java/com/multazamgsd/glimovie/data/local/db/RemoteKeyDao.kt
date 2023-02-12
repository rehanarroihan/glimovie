package com.multazamgsd.glimovie.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.multazamgsd.glimovie.data.local.entity.KeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<KeyEntity>)

    @Query("SELECT * FROM movie_keys WHERE id_key = :id")
    suspend fun getRemoteKeyId(id: String): KeyEntity?

    @Query("DELETE FROM movie_keys")
    suspend fun deleteRemoteKeys()
}