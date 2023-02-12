package com.multazamgsd.glimovie.presentation.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.multazamgsd.glimovie.databinding.ActivityMainBinding
import com.multazamgsd.glimovie.presentation.list.adapter.MovieGridAdapter
import com.multazamgsd.glimovie.presentation.list.adapter.MovieLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()
    private val adapter: MovieGridAdapter by lazy { MovieGridAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.adapter = adapter
        adapter.addLoadStateListener { loadState -> renderUi(loadState) }

        lifecycleScope.launch {
            viewModel.pagingMovies.cachedIn(this).collectLatest { result ->
                adapter.submitData(result)
            }
        }

        binding.rv.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MovieLoadStateAdapter { adapter.retry() },
            footer = MovieLoadStateAdapter { adapter.retry() }
        )

        binding.buttonRetry.setOnClickListener {
            viewModel.getMovies()
        }
    }

    private fun renderUi(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0

        binding.rv.isVisible = !isListEmpty
        binding.tvMoviesEmpty.isVisible = isListEmpty

        binding.rv.isVisible = loadState.source.refresh is LoadState.NotLoading
        binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        binding.buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
    }
}