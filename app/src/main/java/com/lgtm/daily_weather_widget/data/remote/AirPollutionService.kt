package com.lgtm.daily_weather_widget.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface AirPollutionService {

    @GET("air_pollution")
    suspend fun getAirPollution(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appId: String,
    ): AirPollutionDTO?

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
}