package com.multazamgsd.glimovie.utils

import androidx.paging.Pager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

sealed class ResultState<A: Any> {
    class Loading<T: Any>(val message: String? = null) : ResultState<T>()
    class Idle<T: Any> : ResultState<T>()
    data class Success<T: Any>(val data: T?) : ResultState<T>()
    data class Error<T: Any>(val message: String) : ResultState<T>()
}

inline fun <T : Any, U : Any> mapResult(
    resultState: ResultState<out T>,
    mapper: T?.() -> U?
): ResultState<U> {
    return when (resultState) {
        is ResultState.Error -> ResultState.Error(resultState.message)
        is ResultState.Idle -> ResultState.Idle()
        is ResultState.Loading -> ResultState.Loading(resultState.message)
        is ResultState.Success -> {
            val data = resultState.data
            val mapData = mapper.invoke(data)
            ResultState.Success(mapData)
        }
    }
}

fun <T : Any> ResultState<T>.onFailure(result: (String) -> Unit) {
    if (this is ResultState.Error) {
        result.invoke(this.message)
    }
}

fun <T : Any> ResultState<T>.onSuccess(result: (T?) -> Unit) {
    if (this is ResultState.Success) {
        result.invoke(this.data)
    }
}

fun <T : Any> ResultState<T>.onLoading(result: (String?) -> Unit) {
    if (this is ResultState.Loading) {
        result.invoke(this.message)
    }
}

suspend fun <T : Any> fetchPaging(call: suspend () -> Pager<Int, T>): Flow<ResultState<Pager<Int, T>>> =
    flow {
        emit(ResultState.Loading())
        val result = call.invoke()
        emit(ResultState.Success(result))
    }.catch { error ->
        emit(ResultState.Error(error.message.toString()))
    }.flowOn(Dispatchers.IO)

fun <T : Any> idle(): MutableStateFlow<ResultState<T>> = run {
    MutableStateFlow(ResultState.Idle())
}