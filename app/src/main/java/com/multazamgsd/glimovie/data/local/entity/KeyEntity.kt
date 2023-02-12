package com.multazamgsd.glimovie.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("movie_keys")
data class KeyEntity(
    @PrimaryKey
    @ColumnInfo("id_key")
    val id: String,

    @ColumnInfo("prev_key")
    val prevKey: Int?,

    @ColumnInfo("next_key")
    val nextKey: Int?
)
