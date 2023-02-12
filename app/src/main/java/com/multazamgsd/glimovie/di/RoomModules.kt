package com.multazamgsd.glimovie.di

import android.content.Context
import androidx.room.Room
import com.multazamgsd.glimovie.data.local.db.GliMovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModules {

    @Provides
    @Singleton
    fun providesMovieDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, GliMovieDatabase::class.java, "glimovie_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesMoviewDao(database: GliMovieDatabase) = database.movieDao()

    @Provides
    @Singleton
    fun providesRemoteKeyDao(database: GliMovieDatabase) = database.remoteKeyDao()
}