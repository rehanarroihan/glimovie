package com.multazamgsd.glimovie.di

import android.content.Context
import androidx.room.Room
import com.multazamgsd.glimovie.data.local.db.GliMovieDatabase
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

class RoomModules {

    @Provides
    @Singleton
    fun providesStoryDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, GliMovieDatabase::class.java, "glimovie_db")
            .fallbackToDestructiveMigration()
            .build()
}