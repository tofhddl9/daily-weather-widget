package com.lgtm.daily_weather_widget.domain.vo.item

import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.presentation.widgets.WeatherViewType
import com.lgtm.daily_weather_widget.utils.time.timeToSimpleFormat

data class CurrentWeatherSummaryVO(
    override val itemId: Int = WeatherViewType.CURRENT_WEATHER_SUMMARY,
    val temperature: Float = 0f,
    val description: String? = null,
    val hourlyRain: Float = 0f,
    val uvi: Float = 0f,
    val lastUpdate: String? = null,
) : WeatherItemVO

//
//internal fun WeatherVO.mapToCurrentWeatherSummaryVO() = CurrentWeatherSummaryVO(
//    temperature = weather?.temperature ?: 0f,
//    description = weather?.weatherMetaData?.description,
//    hourlyRain = weather?.precipitation ?: 0f,
//    uvi = weather?.uvi ?: 0f,
//    lastUpdate = timeToSimpleFormat(dt, "yy-MM-dd HH:mm:ss") // TODO : move to data module
//)
