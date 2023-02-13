package com.multazamgsd.glimovie.presentation.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multazamgsd.glimovie.databinding.ItemUserReviewBinding
import com.multazamgsd.glimovie.helpers.GeneralHelpers.toTimeFormat
import com.multazamgsd.glimovie.models.UserReview

class UserReviewListAdapter : ListAdapter<UserReview, UserReviewListAdapter.ItemViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<UserReview>() {
            override fun areItemsTheSame(oldItem: UserReview, newItem: UserReview): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserReview, newItem: UserReview): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ItemViewHolder(private val binding: ItemUserReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserReview) {
            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w200${item.authorAvatar}")
                .placeholder(ColorDrawable(Color.GRAY))
                .into(binding.imageAvatar)

            binding.textReview.text = item.review
            binding.textCreated.text = item.createdAt.toTimeFormat()
            binding.textUsername.text = item.authorName.ifEmpty { "Reviewer" }
            binding.textRating.text = item.authorRating.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemUserReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}