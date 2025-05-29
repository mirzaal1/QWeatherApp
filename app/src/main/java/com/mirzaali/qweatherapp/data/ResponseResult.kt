package com.mirzaali.qweatherapp.data


sealed class ResponseResult<out T> {
    data class Success<out T>(val data: T): ResponseResult<T>()
    data class Error(val message: String?, val throwable: Throwable? = null): ResponseResult<Nothing>()
    data object Loading: ResponseResult<Nothing>()
}
