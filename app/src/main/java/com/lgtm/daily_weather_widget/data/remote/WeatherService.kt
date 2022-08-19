package com.lgtm.weathercaster.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appId: String,
        @Query("units") unit: String = TEMPERATURE_UNIT,
    ): WeatherDTO?

    companion object {
        const val TEMPERATURE_UNIT = "metric"
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
}
