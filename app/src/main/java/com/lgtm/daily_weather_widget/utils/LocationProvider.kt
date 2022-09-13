package com.lgtm.daily_weather_widget.utils

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.lgtm.daily_weather_widget.utils.Response
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

interface LocationProvider {
    suspend fun getCurrentLocation(): Response<Location>
    fun getAddress(location: Location): Response<String>
}

@ExperimentalCoroutinesApi
class LocationProviderImpl @Inject constructor(
    private val application: Application,
    private val locationClient: FusedLocationProviderClient
) : LocationProvider {

    override suspend fun getCurrentLocation(): Response<Location> {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!hasAccessFineLocationPermission || !hasAccessCoarseLocationPermission || !isGpsEnabled) {
            return Response.Error(null, "위치 권한이 없습니다")
        }

        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(Response.Success(result), null)
                    } else {
                        cont.resume(Response.Error(null, "위치정보를 얻어오는데 실패했습니다"), null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener { location ->
                    cont.resume(Response.Success(location), null)
                }
                addOnFailureListener {
                    cont.resume(Response.Error(null, "위치정보를 얻어오는데 실패했습니다"), null)
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }

    override fun getAddress(location: Location): Response<String> {
        val position = LatLng(location.latitude, location.longitude)
        val geoCoder = Geocoder(application, Locale.getDefault())
        val address = try {
            geoCoder.getFromLocation(position.latitude, position.longitude, 1).getOrNull(0)
        } catch (e: IOException) {
            return Response.Error(null, "현재 주소를 불러오는데 실패했습니다")
        }

        val addressStr = "${address?.subLocality ?: ""} ${address?.thoroughfare ?: ""}"
        return Response.Success(addressStr)
    }

}
