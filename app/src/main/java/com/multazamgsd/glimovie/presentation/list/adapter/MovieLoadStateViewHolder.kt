package com.multazamgsd.glimovie.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.multazamgsd.glimovie.R
import com.multazamgsd.glimovie.databinding.ItemMoviePosterFooterBinding

class MovieLoadStateViewHolder(
    private val binding: ItemMoviePosterFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnMoviesRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.tvMoviesErrorDescription.text = loadState.error.localizedMessage
        }
        binding.progressMoviesLoadMore.isVisible = loadState is LoadState.Loading
        binding.btnMoviesRetry.isVisible = loadState is LoadState.Error
        binding.tvMoviesErrorDescription.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): MovieLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_poster_footer, parent, false)
            val binding = ItemMoviePosterFooterBinding.bind(view)
            return MovieLoadStateViewHolder(binding, retry)
        }
    }
}