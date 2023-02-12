package com.multazamgsd.glimovie.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.multazamgsd.glimovie.models.Movie

@Entity("movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("is_adult_only")
    val isAdultOnly: Boolean,

    @ColumnInfo("popularity")
    val popularity: Double,

    @ColumnInfo("vote_average")
    val voteAverage: Double,

    @ColumnInfo("vote_count")
    val voteCount: Int,

    @ColumnInfo("image")
    val image: String,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("overview")
    val overview: String,

    @ColumnInfo("release_date")
    val releaseDate: String,

    @ColumnInfo("original_language")
    val originalLanguage: String,
) {
    fun toDomain() = Movie(
        id = id,
        isAdultOnly = isAdultOnly,
        popularity = popularity,
        voteAverage = voteAverage,
        voteCount = voteCount,
        image = image,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        originalLanguage = originalLanguage
    )
}
