package com.lgtm.daily_weather_widget.di

import com.lgtm.daily_weather_widget.utils.LocationProvider
import com.lgtm.daily_weather_widget.utils.LocationProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(
        locationProvider: LocationProviderImpl
    ): LocationProvider
}