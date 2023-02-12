package com.multazamgsd.glimovie.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.multazamgsd.glimovie.data.local.entity.KeyEntity
import com.multazamgsd.glimovie.data.local.entity.MovieEntity

@Database(
    entities = [KeyEntity::class, MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GliMovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}