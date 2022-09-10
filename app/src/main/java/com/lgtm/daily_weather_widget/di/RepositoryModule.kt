package com.lgtm.daily_weather_widget.di

import com.lgtm.daily_weather_widget.data.WeatherDataSource
import com.lgtm.daily_weather_widget.data.WeatherRepositoryImpl
import com.lgtm.daily_weather_widget.data.remote.AirPollutionService
import com.lgtm.daily_weather_widget.data.remote.WeatherRemoteDataSource
import com.lgtm.daily_weather_widget.data.remote.WeatherService
import com.lgtm.daily_weather_widget.domain.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// TODO. Use @Binds of Hilt

@Qualifier
annotation class LocalDataSource

@Qualifier
annotation class RemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

//    @Singleton
//    @Provides
//    fun provideWeatherLocalDataSource(
//        database: WeatherDatabase,
//        ioDispatcher: CoroutineDispatcher
//    ): WeatherDataSource {
//        return WeatherLocalDataSource(database.weatherDao(), ioDispatcher)
//    }

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
        @RemoteDataSource weatherRemoteWeatherDataSource: WeatherDataSource,
        ioDispatcher: CoroutineDispatcher
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherRemoteWeatherDataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
//
//@Module
//@InstallIn(SingletonComponent::class)
//abstract class LocalDataSourceModule {
//
//    @LocalDataSource
//    @Binds
//    abstract fun bindLocalDataSource(impl: WeatherLocalDataSource): WeatherDataSource
//
//}

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @RemoteDataSource
    @Binds
    abstract fun bindRemoteDataSource(impl: WeatherRemoteDataSource): WeatherDataSource

}
