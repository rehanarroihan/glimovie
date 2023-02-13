package com.multazamgsd.glimovie.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.multazamgsd.glimovie.data.local.MovieRemoteMediator
import com.multazamgsd.glimovie.data.local.db.GliMovieDatabase
import com.multazamgsd.glimovie.data.local.entity.MovieEntity
import com.multazamgsd.glimovie.data.remote.network.MovieService
import com.multazamgsd.glimovie.models.MovieTrailer
import com.multazamgsd.glimovie.models.UserReview
import com.multazamgsd.glimovie.utils.ResultState
import com.multazamgsd.glimovie.utils.fetchPaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val service: MovieService,
    private val database: GliMovieDatabase
): MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMovies(): Flow<ResultState<Pager<Int, MovieEntity>>> {
        return fetchPaging {
            Pager(
                config = PagingConfig(
                    pageSize = 5,
                    enablePlaceholders = false,
                    prefetchDistance = 2,
                    initialLoadSize = 20
                ),
                remoteMediator = MovieRemoteMediator(apiService = service, database = database),
                pagingSourceFactory = {
                    database.movieDao().getMovies()
                }
            )
        }
    }

    override suspend fun getUserReviews(movieId: String): Flow<ResultState<List<UserReview>>> {
        return flow {
            emit(ResultState.Loading())

            val dataResult = service.getUserReviews(movieId)
            if (dataResult.isSuccessful && (dataResult.body()?.results?.isNotEmpty()) == true) {
                val finalData = dataResult.body()?.results?.map { it.toDomain() }
                emit(ResultState.Success(finalData))
            } else {
                emit(ResultState.Error(dataResult.errorBody().toString()))
            }
        }.catch { e ->
            emit(ResultState.Error(e.message ?: "an error occurred"))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMovieYoutubeTrailer(movieId: String): Flow<ResultState<MovieTrailer>> {
        return flow {
            emit(ResultState.Loading())

            val dataResult = service.getVideos(movieId)
            if (dataResult.isSuccessful && (dataResult.body()?.results?.isNotEmpty()) == true) {
                var finalResult: MovieTrailer? = null
                val fromYoutube = dataResult.body()?.results?.filter { it.site == "YouTube" }
                val getOneTrailer = fromYoutube?.filter { it.type == "Trailer" }
                finalResult = if (getOneTrailer?.isNotEmpty() == true) {
                    MovieTrailer(
                        id = getOneTrailer.first().id!!,
                        youtubeVideoId = getOneTrailer.first().key!!,
                        type = getOneTrailer.first().type!!,
                        official = getOneTrailer.first().official ?: false,
                    )
                } else {
                    MovieTrailer(
                        id = fromYoutube?.first()?.id!!,
                        youtubeVideoId = fromYoutube.first().key!!,
                        type = fromYoutube.first().type!!,
                        official = fromYoutube.first().official ?: false,
                    )
                }
                emit(ResultState.Success(finalResult))
            } else {
                emit(ResultState.Error(dataResult.errorBody().toString()))
            }
        }.catch { e ->
            emit(ResultState.Error(e.message ?: "an error occurred"))
        }.flowOn(Dispatchers.IO)
    }
}