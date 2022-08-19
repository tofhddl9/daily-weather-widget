package com.lgtm.daily_weather_widget.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "weather")
@JsonClass(generateAdapter = true)
data class WeatherEntity(
    @PrimaryKey val timeZone: String = "",
    val dt: Long = 0,
    val current: WeatherData? = null,
    val dailyWeathers: List<WeatherData>? = null,
    val hourlyWeathers: List<WeatherData>? = null
)

@JsonClass(generateAdapter = true)
data class WeatherData(
    val dt: Long = 0L,
    val temperature: Float = 0f,
    val temperatureMax: Float? = null,
    val temperatureMin: Float? = null,
    val precipitation: Float = 0f,
    val uvi: Float = 0f,
    val weatherMetaData: WeatherMetaData? = null,
)

@JsonClass(generateAdapter = true)
data class WeatherMetaData(
    val id: Int = 0,
    val description: String? = null,
    val icon: String? = null,
)
