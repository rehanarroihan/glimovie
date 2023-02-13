package com.multazamgsd.glimovie.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multazamgsd.glimovie.models.MovieTrailer
import com.multazamgsd.glimovie.models.UserReview
import com.multazamgsd.glimovie.repository.MovieRepository
import com.multazamgsd.glimovie.utils.ResultState
import com.multazamgsd.glimovie.utils.idle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private var _userReviews: MutableStateFlow<ResultState<List<UserReview>>> = idle()
    val userReviews: StateFlow<ResultState<List<UserReview>>> = _userReviews

    private var _trailerData: MutableStateFlow<ResultState<MovieTrailer>> = idle()
    val trailerData: StateFlow<ResultState<MovieTrailer>> = _trailerData

    fun getUserReview(movieId: String) {
        viewModelScope.launch {
            repository.getUserReviews(movieId).collect {
                _userReviews.value = it
            }
        }
    }

    fun getMovieTrailer(movieId: String) {
        viewModelScope.launch {
            repository.getMovieYoutubeTrailer(movieId).collect {
                _trailerData.value = it
            }
        }
    }
}