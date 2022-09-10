package com.lgtm.daily_weather_widget.data.remote

data class AirPollutionDTO(
    val coord: List<Float>? = null,
    val list: List<AirPollution>? = null,
)

data class AirPollution(
    val dt: Long = 0,
    val main: AirPollutionMain? = null,
    val components: AirPollutionData? = null,
)

data class AirPollutionMain(
    // 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor
    val aqi: Int = -1,
)

data class AirPollutionData(
    val pm2_5: Float = 0f,
    val pm10: Float = 0f,
)