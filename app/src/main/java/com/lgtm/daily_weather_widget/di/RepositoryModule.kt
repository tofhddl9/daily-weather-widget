package com.lgtm.daily_weather_widget.di

import com.lgtm.daily_weather_widget.data.WeatherDataSource
import com.lgtm.daily_weather_widget.data.WeatherRepositoryImpl
import com.lgtm.daily_weather_widget.data.remote.AirPollutionService
import com.lgtm.daily_weather_widget.data.remote.WeatherRemoteDataSource
import com.lgtm.daily_weather_widget.data.remote.WeatherService
import com.lgtm.daily_weather_widget.domain.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// TODO. Use @Binds of Hilt
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideWeatherRemoteDataSource(
        weatherService: WeatherService,
        airPollutionService: AirPollutionService,
    ): WeatherDataSource {
        return WeatherRemoteDataSource(weatherService, airPollutionService)
    }

    @Singleton
    @Provides
    fun provideWeatherRepository(
        weatherRemoteWeatherDataSource: WeatherDataSource,
        ioDispatcher: CoroutineDispatcher
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherRemoteWeatherDataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
