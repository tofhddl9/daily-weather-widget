package com.lgtm.daily_weather_widget.domain.vo.item

import com.lgtm.daily_weather_widget.domain.vo.WeatherDataVO
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.presentation.widgets.WeatherViewType

data class HourlyWeatherVO(
    override val itemId: Int = WeatherViewType.HOURLY_WEATHER_SUMMARY,
    val title: String? = null,
    val hourlyWeathers: List<WeatherDataVO>? = null,
) : WeatherItemVO

//internal fun WeatherVO.mapToHourlyWeatherVO() = HourlyWeatherVO(
//    title = "시간별 예보",
//    hourlyWeathers = hourlyWeathers,
//)