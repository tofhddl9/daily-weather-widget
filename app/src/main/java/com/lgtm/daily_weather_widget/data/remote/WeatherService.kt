package com.lgtm.daily_weather_widget.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall/timemachine")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("dt") dt: Long,
        @Query("appid") appId: String,
        @Query("units") unit: String = TEMPERATURE_UNIT,
    ): WeatherDTO?

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/3.0/"
        const val TEMPERATURE_UNIT = "metric"
    }
}
