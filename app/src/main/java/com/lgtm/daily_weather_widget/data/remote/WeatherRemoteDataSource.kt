package com.lgtm.weathercaster.data.remote

import com.lgtm.daily_weather_widget.data.WeatherDataSource
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.data.mapper.mapToWeatherVO
import javax.inject.Inject

private const val KEY = "1e30727449d9657c7b8e82b58b9b49b3"

class WeatherRemoteDataSource @Inject constructor(
    private val weatherService: WeatherService,
) : WeatherDataSource {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherVO? {
        return weatherService.getWeather(latitude, longitude, KEY)?.mapToWeatherVO()
    }

    override suspend fun insertCurrentWeather(weatherVO: WeatherVO) = Unit

    override suspend fun clearCurrentWeather() = Unit

}
