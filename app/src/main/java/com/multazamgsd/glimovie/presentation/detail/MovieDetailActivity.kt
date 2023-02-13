package com.multazamgsd.glimovie.presentation.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.multazamgsd.glimovie.databinding.ActivityMovieDetailBinding
import com.multazamgsd.glimovie.models.Movie
import com.multazamgsd.glimovie.utils.onFailure
import com.multazamgsd.glimovie.utils.onLoading
import com.multazamgsd.glimovie.utils.onSuccess
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE = "EXTRA_MOVIE"
    }

    private lateinit var binding: ActivityMovieDetailBinding
    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var adapter: UserReviewListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_MOVIE, Movie::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_MOVIE)
        }

        viewModel.getMovieTrailer(movie?.id.toString())
        viewModel.getUserReview(movie?.id.toString())

        title = movie?.title
        Glide.with(binding.root)
            .load("https://image.tmdb.org/t/p/original${movie?.image}")
            .into(binding.imagePoster)
        binding.textDescription.text = movie?.overview

        adapter = UserReviewListAdapter()
        binding.rv.adapter = adapter

        lifecycleScope.launch {
            viewModel.trailerData.collect { result ->
                result.onLoading {

                }

                result.onFailure {

                }

                result.onSuccess {
                    showTrailerVideo(it!!.youtubeVideoId)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.userReviews.collect { result ->
                result.onLoading {

                }

                result.onFailure {

                }

                result.onSuccess {
                    adapter.submitList(it)
                }
            }
        }
    }



    private fun showTrailerVideo(youtubeVideoId: String) {
        binding.playerYt.enableAutomaticInitialization = false
        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val defaultPlayerUiController = DefaultPlayerUiController(binding.playerYt, youTubePlayer)
                defaultPlayerUiController.showFullscreenButton(true)
                defaultPlayerUiController.setFullScreenButtonClickListener {
                    if (binding.playerYt.isFullScreen()) {
                        binding.playerYt.exitFullScreen()
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                        if (supportActionBar != null) {
                            supportActionBar!!.show()
                        }
                    } else {
                        binding.playerYt.enterFullScreen()
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                        if (supportActionBar != null) {
                            supportActionBar!!.hide()
                        }
                    }
                }
                binding.playerYt.setCustomPlayerUi(defaultPlayerUiController.rootView)
                youTubePlayer.cueVideo(youtubeVideoId, 0f)
            }
        }
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
        binding.playerYt.initialize(listener, options)
    }
}