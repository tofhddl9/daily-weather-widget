package com.lgtm.daily_weather_widget.domain.vo

data class LocationVO(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String? = null,
)