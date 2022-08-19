package com.lgtm.weathercaster.data.remote

import com.squareup.moshi.Json

data class WeatherDTO(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val timezone: String? = null,
    val current: WeatherData? = null,
    val minutely: List<PrecipitationData>? = null,
    val hourly: List<HourlyWeatherData>? = null,
    val daily: List<DailyWeatherData>? = null,
)

data class WeatherData(
    val dt: Long = 0L,
    val temp: Float = 0f,
    val feels_like: Float = 0f,
    //val humidity: Int,
    val uvi: Float = 0f,
    val clouds: Int = 0,
    //val visibility: Int,
    val wind_speed: Float = 0f,
    val weather: List<WeatherMainData>? = null,
    val rain: DailyRainData? = null,
)

data class WeatherMainData(
    val id: Int = 0,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null,
)

data class PrecipitationData(
    val dt: Long = 0L,
    val precipitation: Float = 0.0f,
)

data class HourlyWeatherData(
    val dt: Long = 0L,
    val temp: Float = 0f,
    val feels_like: Float = 0f,
    //val humidity: Int,
    val uvi: Float = 0f,
    val clouds: Int = 0,
    //val visibility: Int,
    val wind_speed: Float = 0f,
    val weather: List<WeatherMainData>? = null,
    val rain: DailyRainData? = null,
)

data class DailyWeatherData(
    val dt: Long = 0L,
    val temp: DailyTemp? = null,
    val feels_like: DailyTemp? = null,
    //val humidity: Int,
    val uvi: Float = 0f,
    val clouds: Int = 0,
    //val visibility: Int,
    val wind_speed: Float = 0f,
    val weather: List<WeatherMainData>? = null,
    val rain: Float? = null,
)

data class DailyRainData(
    @Json(name = "1h") val hourly: Float = 0f,
)

data class DailyTemp(
    val day: Float = 0f,
    val min: Float = 0f,
    val max: Float = 0f,
    val night: Float = 0f,
    val eve: Float = 0f,
    val morn: Float = 0f,
)
