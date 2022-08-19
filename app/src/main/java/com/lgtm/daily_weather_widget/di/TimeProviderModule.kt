package com.lgtm.daily_weather_widget.di

import com.lgtm.daily_weather_widget.utils.time.SystemTimeProvider
import com.lgtm.daily_weather_widget.utils.time.TimeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimeProviderModule {

    @Provides
    @Singleton
    fun provideTimeProviderModule(): TimeProvider {
        return SystemTimeProvider()
    }
}
