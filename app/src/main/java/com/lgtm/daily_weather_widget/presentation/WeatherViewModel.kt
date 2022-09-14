package com.lgtm.daily_weather_widget.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lgtm.daily_weather_widget.domain.usecase.FetchLocationUseCase
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.domain.usecase.FetchWeatherUseCase
import com.lgtm.daily_weather_widget.presentation.WeatherHomeUiMapper.mapToWeatherHomeUi
import com.lgtm.weathercaster.presentation.WeatherUiEvent
import com.lgtm.daily_weather_widget.utils.Response
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
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val fetchLocationUseCase: FetchLocationUseCase,
    private val timeProvider: TimeProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherHomeState())
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
            val location = fetchLocationUseCase()
            if (location.data == null) {
                onFetchLocationError(location.message)
                return@launch
            }

            val now = timeProvider.getCurrentTimeMillis() / 1000
            fetchWeatherUseCase(location.data.latitude, location.data.longitude, now).collect { weatherData ->
                when(weatherData) {
                    is Response.Success -> {
                        onLoadWeatherSuccess(weatherData.data!!, location.data.address)
                    }
                    is Response.Error -> {
                        onLoadWeatherFailed(weatherData.data, weatherData.message)
                    }
                    is Response.Loading -> {
                        onLoadingWeather(weatherData.isLoading)
                    }
                }
            }
        }
    }

    private fun onLoadWeatherSuccess(data: WeatherVO, address: String?) {
        // TODO. Update HomeUi
        _uiState.value = _uiState.value.copy(
            uiData = data.mapToWeatherHomeUi(address)
        )
    }

    private fun onLoadWeatherFailed(data: WeatherVO?, message: String?) {
        onError(message ?: "Load Weather Error")
    }

    private fun onFetchLocationError(message: String?) {
        onError(message ?: "Fetch Location Error")
    }

    private fun onLoadingWeather(loading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = loading)
    }

    private fun onError(message: String) {
        viewModelScope.launch {
            errorMessageChannel.send(message)
        }
    }

}
