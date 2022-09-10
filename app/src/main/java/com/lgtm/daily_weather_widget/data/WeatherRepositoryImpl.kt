package com.lgtm.daily_weather_widget.data

import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.di.RemoteDataSource
import com.lgtm.daily_weather_widget.domain.WeatherRepository
import com.lgtm.daily_weather_widget.utils.Response
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// TODO. Move to usecase
private const val ONE_DAY_IN_SEC = 60 * 60 * 24

class WeatherRepositoryImpl @Inject constructor(
    @RemoteDataSource private val weatherRemoteDataSource: WeatherDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : WeatherRepository {

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        dt: Long,
        fetchFromRemote: Boolean
    ): Flow<Response<WeatherVO>> {
        return flow {
            emit(Response.Loading(true))

            var todayWeather: WeatherVO? = null
            todayWeather = try {
                weatherRemoteDataSource.getWeather(latitude, longitude, dt)
            } catch(e: Exception) {
                e.printStackTrace()
                emit(Response.Error(data = todayWeather,"날씨 정보 업데이트를 실패했습니다."))
                null
            }

            var yesterdayWeather: WeatherVO? = null
            yesterdayWeather = try {
                weatherRemoteDataSource.getWeather(latitude, longitude, dt - ONE_DAY_IN_SEC)
            } catch(e: Exception) {
                e.printStackTrace()
                emit(Response.Error(data = yesterdayWeather,"날씨 정보 업데이트를 실패했습니다."))
                null
            }

            if (todayWeather == null || yesterdayWeather == null) {
                emit(Response.Error(data = todayWeather, message = "날씨 정보 업데이트를 실패했습니다."))
                return@flow
            }

            todayWeather = todayWeather.copy(
                yesterdayTemperature = yesterdayWeather.todayWeather?.temperature!!
            )

            emit(Response.Success(data = todayWeather))
            emit(Response.Loading(false))
        }
    }

}