package com.lgtm.weathercaster.presentation

sealed class WeatherUiEvent {
    object Refresh : WeatherUiEvent()
}