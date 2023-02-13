package com.multazamgsd.glimovie.data.remote.response

import com.google.gson.annotations.SerializedName
import com.multazamgsd.glimovie.models.UserReview

data class UserReviewResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<Results> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
) {
    data class AuthorDetails(
        @SerializedName("name") var name: String? = null,
        @SerializedName("username") var username: String? = null,
        @SerializedName("avatar_path") var avatarPath: String? = null,
        @SerializedName("rating") var rating: Double? = null
    )

    data class Results(
        @SerializedName("author") var author: String? = null,
        @SerializedName("author_details") var authorDetails: AuthorDetails? = AuthorDetails(),
        @SerializedName("content") var content: String? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("id") var id: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null,
        @SerializedName("url") var url: String? = null
    ) {
        fun toDomain() = UserReview(
            id = id!!,
            authorName = authorDetails?.name ?: "Reviewer",
            authorAvatar = authorDetails?.avatarPath ?: "",
            authorRating = authorDetails?.rating ?: 0.0,
            review = content ?: "",
            createdAt = createdAt ?: ""
        )
    }
}