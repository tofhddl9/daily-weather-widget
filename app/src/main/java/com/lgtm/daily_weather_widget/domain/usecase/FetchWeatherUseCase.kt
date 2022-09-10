package com.lgtm.daily_weather_widget.domain.usecase

import com.lgtm.daily_weather_widget.domain.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class FetchWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository,
) {

    suspend operator fun invoke(latitude: Double, longitude: Double, dt: Long) =
        repository.getWeather(latitude, longitude, dt, true)
}