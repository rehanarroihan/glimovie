package com.multazamgsd.glimovie.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.multazamgsd.glimovie.data.local.entity.KeyEntity
import com.multazamgsd.glimovie.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<MovieEntity>)

    @Query("SELECT * FROM movies WHERE id_key = :id")
    suspend fun getRemoteKeyId(id: String): MovieEntity?

    @Query("DELETE FROM movies")
    suspend fun deleteRemoteKeys()
}