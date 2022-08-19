package com.lgtm.daily_weather_widget.data.mapper

import com.lgtm.daily_weather_widget.data.local.WeatherData
import com.lgtm.daily_weather_widget.data.local.WeatherEntity
import com.lgtm.daily_weather_widget.data.local.WeatherMetaData
import com.lgtm.daily_weather_widget.domain.vo.WeatherDataVO
import com.lgtm.daily_weather_widget.domain.vo.WeatherMetaDataVO
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO

fun WeatherEntity.mapToWeatherVO() = WeatherVO(
    dt = dt,
    timeZone = timeZone,
    current = current?.mapToWeatherDataVO(),
    dailyWeathers = dailyWeathers?.map { it.mapToWeatherDataVO() },
    hourlyWeathers = hourlyWeathers?.map { it.mapToWeatherDataVO() },
)

fun WeatherVO.mapToWeatherEntity() = WeatherEntity(
    dt = dt,
    timeZone = timeZone ?: "",
    current = current?.mapToWeatherData(),
    dailyWeathers = dailyWeathers?.map { it.mapToWeatherData() },
    hourlyWeathers = hourlyWeathers?.map { it.mapToWeatherData() },
)

private fun WeatherData.mapToWeatherDataVO() = WeatherDataVO(
    dt = dt,
    temperature = temperature,
    temperatureMax = temperatureMax,
    temperatureMin = temperatureMin,
    precipitation = precipitation,
    uvi = uvi,
    weatherMetaData = weatherMetaData?.mapToWeatherMetaDataVO()
)

private fun WeatherDataVO.mapToWeatherData() = WeatherData(
    dt = dt,
    temperature = temperature,
    temperatureMax = temperatureMax,
    temperatureMin = temperatureMin,
    precipitation = precipitation,
    uvi = uvi,
    weatherMetaData = weatherMetaData?.mapToWeatherMetaData()
)

private fun WeatherMetaData.mapToWeatherMetaDataVO() = WeatherMetaDataVO(
    id = id,
    description = description,
    icon = icon
)

private fun WeatherMetaDataVO.mapToWeatherMetaData() = WeatherMetaData(
    id = id,
    description = description,
    icon = icon
)
