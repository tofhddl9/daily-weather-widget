package com.lgtm.daily_weather_widget.domain.vo.item

import com.lgtm.daily_weather_widget.domain.vo.WeatherDataVO
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.presentation.widgets.WeatherViewType

data class DailyWeatherVO(
    override val itemId: Int = WeatherViewType.DAILY_WEATHER_SUMMARY,
    val title: String? = null,
    val dailyWeathers: List<WeatherDataVO>? = null,
) : WeatherItemVO

internal fun WeatherVO.mapToDailyWeatherVO() = DailyWeatherVO(
    title = "${dailyWeathers?.size}일간의 일기예보",
    dailyWeathers = dailyWeathers
)
