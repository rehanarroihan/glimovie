package com.multazamgsd.glimovie.data.remote.response

import com.google.gson.annotations.SerializedName
import com.multazamgsd.glimovie.data.local.entity.MovieEntity
import com.multazamgsd.glimovie.models.Movie

data class MovieResponse(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<Results> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
) {
    data class Results(
        @SerializedName("adult") var adult: Boolean? = null,
        @SerializedName("backdrop_path") var backdropPath: String? = null,
        @SerializedName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
        @SerializedName("id") var id: Int? = null,
        @SerializedName("original_language") var originalLanguage: String? = null,
        @SerializedName("original_title") var originalTitle: String? = null,
        @SerializedName("overview") var overview: String? = null,
        @SerializedName("popularity") var popularity: Double? = null,
        @SerializedName("poster_path") var posterPath: String? = null,
        @SerializedName("release_date") var releaseDate: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("video") var video: Boolean? = null,
        @SerializedName("vote_average") var voteAverage: Double? = null,
        @SerializedName("vote_count") var voteCount: Int? = null
    ) {
        fun toEntity() = MovieEntity(
            id = id!!,
            isAdultOnly = adult ?: false,
            popularity = popularity!!,
            voteAverage = voteAverage!!,
            voteCount = voteCount!!,
            image = posterPath!!,
            title = title!!,
            overview = overview!!,
            releaseDate = releaseDate!!,
            originalLanguage = originalLanguage!!
        )
    }
}