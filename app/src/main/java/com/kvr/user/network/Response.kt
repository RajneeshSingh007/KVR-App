package com.kvr.user.network

sealed class Response<T>(val data: T? = null, var error: String = "", var isLoading: Boolean = false) {
    class Empty<T> : Response<T>()
    class Loading<T>(isLoading: Boolean = false) : Response<T>(isLoading =isLoading)
    class Success<T>(data: T? = null) : Response<T>(data = data)
    class Error<T>(error: String = "") : Response<T>(error = error)
}