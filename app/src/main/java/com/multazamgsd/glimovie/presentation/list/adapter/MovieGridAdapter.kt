package com.multazamgsd.glimovie.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multazamgsd.glimovie.databinding.ItemMoviePosterBinding
import com.multazamgsd.glimovie.models.Movie

class MovieGridAdapter : PagingDataAdapter<Movie, MovieGridAdapter.MoviePosterViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        val binding = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviePosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
        holder.bind(getItem(position)?.image)
    }

    inner class MoviePosterViewHolder(
        private val binding: ItemMoviePosterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(path: String?) {
            path?.let {
                Glide.with(binding.root)
                    .load("https://image.tmdb.org/t/p/w500/$it")
                    .into(binding.ivMoviePoster)
            }
        }
    }
}