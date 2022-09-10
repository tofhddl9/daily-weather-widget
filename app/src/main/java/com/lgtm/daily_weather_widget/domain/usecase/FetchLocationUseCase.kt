package com.lgtm.daily_weather_widget.domain.usecase

import android.location.Location
import com.lgtm.daily_weather_widget.domain.vo.LocationVO
import com.lgtm.daily_weather_widget.utils.LocationProvider
import com.lgtm.daily_weather_widget.utils.Response
import com.lgtm.daily_weather_widget.utils.Response.Companion.failure
import com.lgtm.daily_weather_widget.utils.Response.Companion.success
import javax.inject.Inject

class FetchLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider,
) {
    suspend operator fun invoke(): Response<LocationVO> {
        return when (val location = locationProvider.getCurrentLocation()) {
            is Response.Success -> {
                location.withAddress()
            }
            else -> {
                failure(msg = location.message ?: "Fail to load location - error")
            }
        }
    }

    private fun Response<Location>.withAddress(): Response<LocationVO> {
        data ?: return failure(msg = message ?: "Fail to load location - empty")

        return when (val address = locationProvider.getAddress(data)) {
            is Response.Success -> {
                success(LocationVO(
                    latitude = data.latitude,
                    longitude = data.longitude,
                    address = address.data
                ))
            }
            else -> {
                failure(msg = address.message ?: "Fail to load address")
            }
        }
    }
}