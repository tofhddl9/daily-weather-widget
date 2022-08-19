package com.lgtm.daily_weather_widget.domain.vo

data class WeatherVO(
    val dt: Long = 0,
    val timeZone: String? = null,
    val current: WeatherDataVO? = null,
    val dailyWeathers: List<WeatherDataVO>? = null,
    val hourlyWeathers: List<WeatherDataVO>? = null
)

data class WeatherDataVO(
    val dt: Long = 0L,
    val temperature: Float = 0f,
    val temperatureMax: Float? = null,
    val temperatureMin: Float? = null,
    val precipitation: Float = 0f,
    val uvi: Float = 0f,
    val weatherMetaData: WeatherMetaDataVO? = null,
)

data class WeatherMetaDataVO(
    val id: Int = 0,
    val description: String? = null,
    val icon: String? = null,
)
