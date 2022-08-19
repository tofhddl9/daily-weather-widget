package com.lgtm.daily_weather_widget.data.mapper

import com.lgtm.daily_weather_widget.domain.vo.WeatherDataVO
import com.lgtm.daily_weather_widget.domain.vo.WeatherMetaDataVO
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.weathercaster.data.remote.DailyWeatherData
import com.lgtm.weathercaster.data.remote.HourlyWeatherData
import com.lgtm.weathercaster.data.remote.WeatherDTO
import com.lgtm.weathercaster.data.remote.WeatherData
import com.lgtm.weathercaster.data.remote.WeatherMainData
import com.lgtm.daily_weather_widget.utils.time.SystemTimeProvider

fun WeatherDTO.mapToWeatherVO() = WeatherVO(
    dt = SystemTimeProvider().getCurrentTimeMillis(),
    timeZone = timezone,
    current = current?.mapToWeatherDataVO(),
    dailyWeathers = daily?.mapToDailyWeatherDataVO(),
    hourlyWeathers = hourly?.subList(0, 24)?.mapToHourlyWeatherDataVO(),
)

private fun WeatherData.mapToWeatherDataVO() = WeatherDataVO(
    dt = dt * 1000L,
    temperature = temp,
    precipitation = rain?.hourly ?: 0f,
    uvi = uvi,
    weatherMetaData = weather?.firstOrNull()?.mapToWeatherMetaDataVO(),
)

private fun WeatherMainData.mapToWeatherMetaDataVO() = WeatherMetaDataVO(
    id = id,
    description = main,
    icon = "https://openweathermap.org/img/wn/$icon@2x.png"
)

private fun List<DailyWeatherData>.mapToDailyWeatherDataVO() = this.map {
    WeatherDataVO(
        dt = it.dt * 1000L,
        temperature = it.temp?.day ?: 0f,
        temperatureMin = it.temp?.min ?: 0f,
        temperatureMax = it.temp?.max ?: 0f,
        precipitation = it.rain ?: 0f,
        uvi = it.uvi,
        weatherMetaData = it.weather?.firstOrNull()?.mapToWeatherMetaDataVO(),
    )
}

private fun List<HourlyWeatherData>.mapToHourlyWeatherDataVO() = this.map {
    WeatherDataVO(
        dt = it.dt * 1000L,
        temperature = it.temp,
        precipitation = it.rain?.hourly ?: 0f,
        uvi = it.uvi,
        weatherMetaData = it.weather?.firstOrNull()?.mapToWeatherMetaDataVO(),
    )
}
