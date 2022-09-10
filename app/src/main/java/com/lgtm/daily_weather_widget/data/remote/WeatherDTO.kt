package com.lgtm.daily_weather_widget.data.remote

data class WeatherDTO(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val timezone: String? = null,
    val data: List<WeatherData>? = null,
)

data class WeatherData(
    val dt: Long = 0L,
    val temp: Float = 0f,
    val feels_like: Float = 0f,
    //val humidity: Int,
    val uvi: Float = 0f,
    val clouds: Int = 0,
    val wind_speed: Float = 0f,
    val weather: List<WeatherMainData>? = null,
    // val rain: DailyRainData? = null,
)

data class WeatherMainData(
    val id: Int = 0,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null,
)
