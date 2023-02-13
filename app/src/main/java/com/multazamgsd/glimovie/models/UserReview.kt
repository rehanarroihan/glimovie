package com.multazamgsd.glimovie.models

data class UserReview(
    val id: String,
    val authorName: String,
    val authorAvatar: String,
    val authorRating: Double,
    val review: String,
    val createdAt: String,
)
