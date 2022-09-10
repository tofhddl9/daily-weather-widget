package com.lgtm.daily_weather_widget.domain

import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.utils.Response
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        dt: Long,
        fetchFromRemote: Boolean,
    ): Flow<Response<WeatherVO>>

}