package com.lgtm.daily_weather_widget.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.lgtm.daily_weather_widget.data.local.WeatherData
import com.lgtm.daily_weather_widget.data.local.WeatherEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class WeatherEntityTypeConverter(
    private val moshi: Moshi
) {

    @TypeConverter
    fun fromString(value: String): WeatherEntity? {
        val adapter: JsonAdapter<WeatherEntity> = moshi.adapter(WeatherEntity::class.java)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromWeatherData(type: WeatherEntity?): String {
        val adapter: JsonAdapter<WeatherEntity> = moshi.adapter(WeatherEntity::class.java)
        return adapter.toJson(type)
    }

}

@ProvidedTypeConverter
class WeatherDataTypeConverter(
    private val moshi: Moshi
) {

    @TypeConverter
    fun fromString(value: String): WeatherData? {
        val adapter: JsonAdapter<WeatherData> = moshi.adapter(WeatherData::class.java)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromWeatherData(type: WeatherData?): String {
        val adapter: JsonAdapter<WeatherData> = moshi.adapter(WeatherData::class.java)
        return adapter.toJson(type)
    }

}

@ProvidedTypeConverter
class WeatherDataListTypeConverter(
    private val moshi: Moshi
) {

    @TypeConverter
    fun fromString(value: String): List<WeatherData>? {
        val listType = Types.newParameterizedType(List::class.java, WeatherData::class.java)
        val adapter: JsonAdapter<List<WeatherData>> = moshi.adapter(listType)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromWeatherDataList(type: List<WeatherData>?): String {
        val listType = Types.newParameterizedType(List::class.java, WeatherData::class.java)
        val adapter: JsonAdapter<List<WeatherData>> = moshi.adapter(listType)
        return adapter.toJson(type)
    }

}
