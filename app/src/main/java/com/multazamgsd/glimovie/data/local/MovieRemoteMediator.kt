package com.multazamgsd.glimovie.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.multazamgsd.glimovie.data.local.db.GliMovieDatabase
import com.multazamgsd.glimovie.data.local.entity.KeyEntity
import com.multazamgsd.glimovie.data.local.entity.MovieEntity
import com.multazamgsd.glimovie.data.remote.network.MovieService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val database: GliMovieDatabase,
    private val apiService: MovieService
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey =
                    remoteKeys?.prevKey ?: return MediatorResult.Success(remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey =
                    remoteKeys?.nextKey ?: return MediatorResult.Success(remoteKeys != null)
                nextKey
            }
        }
        return try {
            val responseData = apiService.getMovies(page).body()?.results ?: arrayListOf()
            val endOfPaginationReached = responseData.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeyDao().delete()
                    database.movieDao().delete()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.map { movieResponse ->
                    KeyEntity(
                        id = movieResponse.id.toString(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                database.remoteKeyDao().insertAll(keys)
                database.movieDao().insertAll(responseData.map {
                    it.toEntity()
                })
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): KeyEntity? {
        return state.pages.lastOrNull { pagingSource ->
            pagingSource.data.isNotEmpty()
        }?.data
            ?.lastOrNull()
            ?.let { data ->
                database.remoteKeyDao().getRemoteKeyId(data.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): KeyEntity? {
        return state.pages.firstOrNull { pagingSource ->
            pagingSource.data.isNotEmpty()
        }?.data
            ?.firstOrNull()
            ?.let { data ->
                database.remoteKeyDao().getRemoteKeyId(data.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): KeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeyDao().getRemoteKeyId(id)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private companion object {
        const val INITIAL_PAGE = 1
    }
}