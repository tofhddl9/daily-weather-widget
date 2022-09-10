package com.lgtm.daily_weather_widget.data.mapper

import com.lgtm.daily_weather_widget.domain.vo.WeatherDataVO
import com.lgtm.daily_weather_widget.domain.vo.WeatherMetaDataVO
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.data.remote.WeatherDTO
import com.lgtm.daily_weather_widget.data.remote.WeatherData
import com.lgtm.daily_weather_widget.data.remote.WeatherMainData

fun WeatherDTO.mapToWeatherVO() = WeatherVO(
    timeZone = timezone,
    todayWeather = data?.firstOrNull()?.mapToWeatherDataVO()
)

private fun WeatherData.mapToWeatherDataVO() = WeatherDataVO(
    dt = dt * 1000L,
    temperature = temp,
    feelsLike = feels_like,
    uvi = uvi,
    clouds = clouds,
    weatherMetaData = weather?.firstOrNull()?.mapToWeatherMetaDataVO(),
)

private fun WeatherMainData.mapToWeatherMetaDataVO() = WeatherMetaDataVO(
    id = id,
    description = "${main}(${description}})",
    icon = "https://openweathermap.org/img/wn/$icon@2x.png"
)
