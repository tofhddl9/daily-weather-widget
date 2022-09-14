package com.lgtm.daily_weather_widget.presentation

import com.lgtm.daily_weather_widget.R
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.utils.time.SystemTimeProvider

data class WeatherHomeState(
    val uiData: WeatherHomeUi? = null,
    val isLoading: Boolean = false,
)

data class WeatherHomeUi(
    val title: String? = null,
    val message: String? = null,
    val address: String? = null,
    val weatherIcon: String? = null,
    val mainImageRes: Int = -1,
    val lastUpdateDate: String? = null,
)

object WeatherHomeUiMapper {

    fun WeatherVO.mapToWeatherHomeUi(
        address: String? = null,
    ) = WeatherHomeUi(
        title = mapToWeatherTitle(todayWeather?.temperature!!, yesterdayTemperature),
        message = "${yesterdayTemperature}\u00B0C     >>     ${todayWeather.temperature}\u00B0C",
        address = address,
        weatherIcon = todayWeather.weatherMetaData?.icon,
        mainImageRes = mapToWeatherMainImageRes(todayWeather.temperature, yesterdayTemperature),
        lastUpdateDate = SystemTimeProvider().getCurrentTimeInISO8601(todayWeather.dt),
    )

    // TODO. split another class
    // TODO. make user can set threshold in preference page
    private fun mapToWeatherTitle(todayTemp: Float, yesterdayTemp: Float): String {
        val dailyTempDiff = todayTemp - yesterdayTemp
        return if (-THRESHOLD <= dailyTempDiff && dailyTempDiff < THRESHOLD) {
            "어제와\n비슷해요"
        } else if (dailyTempDiff < -THRESHOLD) {
            if (todayTemp < 10) "어제보다\n시원해요"
            else "어제보다\n추워요"
        } else {
            if (todayTemp < 20) "어제보다\n따듯해요"
            else "어제보다\n더워요"
        }

    }

    // TODO. Refactor
    private fun mapToWeatherMainImageRes(todayTemp: Float, yesterdayTemp: Float): Int {
        val dailyTempDiff = todayTemp - yesterdayTemp
        return if (-THRESHOLD <= dailyTempDiff && dailyTempDiff < THRESHOLD) {
            R.drawable.slowbro_flat
        } else if (dailyTempDiff < -THRESHOLD) {
            R.drawable.slowbro_decrease
        } else {
            R.drawable.slowbro_increase
        }

    }

    private const val THRESHOLD = 2.0f

}