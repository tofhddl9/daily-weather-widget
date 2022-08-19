package com.lgtm.daily_weather_widget.presentation

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.domain.vo.item.mapToCurrentWeatherSummaryVO
import com.lgtm.daily_weather_widget.domain.vo.item.mapToDailyWeatherVO
import com.lgtm.daily_weather_widget.domain.vo.item.mapToHourlyWeatherVO
import com.lgtm.daily_weather_widget.domain.WeatherRepository
import com.lgtm.daily_weather_widget.utils.LocationProvider
import com.lgtm.weathercaster.presentation.WeatherUiEvent
import com.lgtm.daily_weather_widget.utils.Response
import com.lgtm.daily_weather_widget.utils.time.SystemTimeProvider
import com.lgtm.daily_weather_widget.utils.time.TimeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationProvider: LocationProvider,
    private val timeProvider: TimeProvider = SystemTimeProvider(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    private val errorMessageChannel = Channel<String>()
    val errorMessages = errorMessageChannel.receiveAsFlow()

    fun onEvent(event: WeatherUiEvent) {
        when (event) {
            WeatherUiEvent.Refresh -> {
                getCurrentWeather(fetchFromRemote = true)
            }
        }
    }

    fun getCurrentWeather(fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            when (val location = locationProvider.getCurrentLocation()) {
                is Response.Success -> {
                    location.data?.also {
                        updateLocationInfo(it)
                        loadWeather(it.latitude, it.longitude, true)
                    } ?: run {
                        // never called?
                        onLocationLoadFailed("NEVER CALLED")
                    }
                }
                else -> run {
                    onLocationLoadFailed(location.message!!)
                }
            }
        }
    }

    private fun onLocationLoadFailed(msg: String) {
        viewModelScope.launch {
            errorMessageChannel.send(msg)
        }
    }

    private fun updateLocationInfo(location: Location) {
        when (val address = locationProvider.getAddress(location)) {
            is Response.Success -> {
                _uiState.value = _uiState.value.copy(
                    location = address.data
                )
            }
            else -> {
                _uiState.value = _uiState.value.copy(
                    location = "",
                )
                viewModelScope.launch {
                    errorMessageChannel.send(address.message ?: "")
                }
            }
        }
    }

    private suspend fun loadWeather(latitude: Double, longitude: Double, fetchFromRemote: Boolean) {
        viewModelScope.launch {
            repository.getCurrentWeather(latitude, longitude, true).collect { response ->
                when(response) {
                    is Response.Success -> {
                        onLoadWeatherSuccess(response.data!!)
                    }
                    is Response.Error -> {
                        onLoadWeatherFailed(response.data, response.message)
                    }
                    is Response.Loading -> {
                        onLoadingWeather(response.isLoading)
                    }
                }
            }
        }
    }

    private fun onLoadWeatherSuccess(data: WeatherVO) {
        val currentTime = data.current?.dt ?: timeProvider.getCurrentTimeMillis()
        val mainWeather = data.current?.weatherMetaData?.description ?: ""
        val weatherState = WeatherState.UNKNOWN

        _uiState.value = _uiState.value.copy(
            weatherWidgets = listOf(
                data.mapToCurrentWeatherSummaryVO(),
                data.mapToHourlyWeatherVO(),
                data.mapToDailyWeatherVO(),
                data.mapToDailyWeatherVO(),
            ),
            weatherState = weatherState
        )
    }

    private fun onLoadWeatherFailed(data: WeatherVO?, message: String?) {
        viewModelScope.launch {
            errorMessageChannel.send(message ?: "")
        }
    }

    private fun onLoadingWeather(loading: Boolean) {
        _uiState.value = _uiState.value.copy(
            isLoading = loading
        )
    }


}