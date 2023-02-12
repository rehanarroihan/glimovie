package com.multazamgsd.glimovie.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.multazamgsd.glimovie.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MovieGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieGridAdapter()
        binding.rvMovies.adapter = MovieGridAdapter()
        adapter.addLoadStateListener { loadState -> renderUi(loadState) }

        lifecycleScope.launch {
            viewModel.pagingMovies.cachedIn(this).collect { result ->
                adapter.submitData(result)
            }
        }

        binding.btnMoviesRetry.setOnClickListener {
            viewModel.getMovies()
        }
    }

    private fun renderUi(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0

        binding.rvMovies.isVisible = !isListEmpty
        binding.tvMoviesEmpty.isVisible = isListEmpty

        binding.rvMovies.isVisible = loadState.source.refresh is LoadState.NotLoading
        binding.progressBarMovies.isVisible = loadState.source.refresh is LoadState.Loading
        binding.btnMoviesRetry.isVisible = loadState.source.refresh is LoadState.Error
    }
}