package com.multazamgsd.glimovie.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.multazamgsd.glimovie.models.Movie
import com.multazamgsd.glimovie.repository.MovieRepository
import com.multazamgsd.glimovie.utils.onFailure
import com.multazamgsd.glimovie.utils.onLoading
import com.multazamgsd.glimovie.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private var _pagingMovies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty())
    val pagingMovies: StateFlow<PagingData<Movie>> = _pagingMovies

    private var _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var _errorMessage: Channel<String> = Channel()
    val errorMessage = _errorMessage.receiveAsFlow()

    init {
        getMovies()
    }

    fun getMovies() = viewModelScope.launch {
        repository.getMovies().collect { resultState ->
            resultState.onSuccess { pager ->
                _loading.value = false
                viewModelScope.launch {
                    pager?.flow?.collect { pagingData ->
                        _pagingMovies.value = pagingData.map { movieEntity ->
                            movieEntity.toDomain()
                        }
                    }
                }
            }

            resultState.onLoading {
                _loading.value = true
            }

            resultState.onFailure { message ->
                _loading.value = false
                viewModelScope.launch {
                    _errorMessage.send(message)
                }
            }

        }
    }
}