package com.lgtm.daily_weather_widget.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lgtm.daily_weather_widget.data.local.converter.WeatherDataListTypeConverter
import com.lgtm.daily_weather_widget.data.local.converter.WeatherDataTypeConverter
import com.lgtm.daily_weather_widget.data.local.converter.WeatherEntityTypeConverter

@TypeConverters(
    value = [
        WeatherEntityTypeConverter::class,
        WeatherDataListTypeConverter::class,
        WeatherDataTypeConverter::class,
    ]
)
@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

}