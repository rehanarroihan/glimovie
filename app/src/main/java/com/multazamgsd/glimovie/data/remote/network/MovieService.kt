package com.multazamgsd.glimovie.data.remote.network

import com.multazamgsd.glimovie.data.remote.response.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/top_rated")
    suspend fun getMovies(
        @Query("page") page: Int
    ): Response<MovieResponse>
}