package com.multazamgsd.glimovie.repository

import androidx.paging.Pager
import com.multazamgsd.glimovie.data.local.entity.MovieEntity
import com.multazamgsd.glimovie.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovies(): Flow<ResultState<Pager<Int, MovieEntity>>>
}