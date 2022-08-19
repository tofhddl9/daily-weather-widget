package com.lgtm.daily_weather_widget.data

import com.lgtm.daily_weather_widget.domain.vo.WeatherVO

interface WeatherDataSource {

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
    ): WeatherVO?

    suspend fun insertCurrentWeather(weatherVO: WeatherVO)

    suspend fun clearCurrentWeather()

}