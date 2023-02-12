package com.multazamgsd.glimovie.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
)
