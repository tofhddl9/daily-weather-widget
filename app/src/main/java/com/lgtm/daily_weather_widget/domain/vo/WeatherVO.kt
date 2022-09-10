package com.lgtm.daily_weather_widget.domain.vo

data class WeatherVO(
    val timeZone: String? = null,
    val todayWeather: WeatherDataVO? = null,
    val yesterdayTemperature: Float = 0f,
)

data class WeatherDataVO(
    val dt: Long = 0L,
    val temperature: Float = 0f,
    val feelsLike: Float= 0f,
    val uvi: Float = 0f,
    val clouds: Int = 0,
    val weatherMetaData: WeatherMetaDataVO? = null,
)

data class WeatherMetaDataVO(
    val id: Int = 0,
    val description: String? = null,
    val icon: String? = null,
)
