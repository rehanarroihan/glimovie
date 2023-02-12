package com.multazamgsd.glimovie.di

import com.multazamgsd.glimovie.repository.MovieRepository
import com.multazamgsd.glimovie.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModules {

    @Binds
    abstract fun provideMovieRepository(repository: MovieRepositoryImpl): MovieRepository
}