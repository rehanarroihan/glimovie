package com.multazamgsd.glimovie.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.multazamgsd.glimovie.data.local.MovieRemoteMediator
import com.multazamgsd.glimovie.data.local.db.GliMovieDatabase
import com.multazamgsd.glimovie.data.local.entity.MovieEntity
import com.multazamgsd.glimovie.data.remote.network.MovieService
import com.multazamgsd.glimovie.utils.ResultState
import com.multazamgsd.glimovie.utils.fetchPaging
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val service: MovieService,
    private val database: GliMovieDatabase
): MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMovies(): Flow<ResultState<Pager<Int, MovieEntity>>> {
        return fetchPaging {
            Pager(
                config = PagingConfig(pageSize = 5),
                remoteMediator = MovieRemoteMediator(apiService = service, database = database),
                pagingSourceFactory = {
                    database.movieDao().getMovies()
                }
            )
        }
    }
}