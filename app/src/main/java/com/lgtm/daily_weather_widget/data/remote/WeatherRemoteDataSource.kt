package com.lgtm.daily_weather_widget.data.remote

import com.lgtm.daily_weather_widget.data.WeatherDataSource
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.data.mapper.mapToWeatherVO
import javax.inject.Inject
import kotlinx.coroutines.flow.zip

private const val KEY = "016ba9990ffbb5065e21e82c40720bd3"

class WeatherRemoteDataSource @Inject constructor(
    private val weatherService: WeatherService,
    private val airPollutionService: AirPollutionService,
) : WeatherDataSource {

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        dt: Long
    ): WeatherVO? {
        // TODO : zip the two apis and return vo
        // val weather = weatherService.getWeather(latitude, longitude, dt, KEY)
        // val air = airPollutionService.getAirPollution(latitude, longitude, KEY)

        return weatherService.getWeather(latitude, longitude, dt, KEY)?.mapToWeatherVO()
    }

//    override suspend fun insertCurrentWeather(weatherVO: WeatherVO) = Unit
//
//    override suspend fun clearCurrentWeather() = Unit

}
