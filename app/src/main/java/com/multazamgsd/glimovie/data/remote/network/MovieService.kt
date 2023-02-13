package com.multazamgsd.glimovie.data.remote.network

import com.multazamgsd.glimovie.data.remote.response.MovieResponse
import com.multazamgsd.glimovie.data.remote.response.MovieVideosResponse
import com.multazamgsd.glimovie.data.remote.response.UserReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/top_rated")
    suspend fun getMovies(
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/reviews")
    suspend fun getUserReviews(
        @Path("movie_id") id: String,
    ): Response<UserReviewResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") id: String,
    ): Response<MovieVideosResponse>
}