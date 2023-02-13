package com.multazamgsd.glimovie.repository

import androidx.paging.Pager
import com.multazamgsd.glimovie.data.local.entity.MovieEntity
import com.multazamgsd.glimovie.data.remote.response.MovieVideosResponse
import com.multazamgsd.glimovie.data.remote.response.UserReviewResponse
import com.multazamgsd.glimovie.models.MovieTrailer
import com.multazamgsd.glimovie.models.UserReview
import com.multazamgsd.glimovie.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovies(): Flow<ResultState<Pager<Int, MovieEntity>>>

    suspend fun getUserReviews(movieId: String): Flow<ResultState<List<UserReview>>>

    suspend fun getMovieYoutubeTrailer(movieId: String): Flow<ResultState<MovieTrailer>>
}