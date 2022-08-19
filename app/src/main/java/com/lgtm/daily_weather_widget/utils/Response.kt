package com.lgtm.daily_weather_widget.utils

sealed class Response<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T?) : Response<T>(data)

    class Error<T>(data: T?, message: String) : Response<T>(data, message)

    class Loading<T>(val isLoading: Boolean = true): Response<T>(null)

}
