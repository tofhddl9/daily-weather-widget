package com.lgtm.daily_weather_widget.presentation.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.SizeF
import android.widget.RemoteViews
import com.lgtm.daily_weather_widget.R
import com.lgtm.daily_weather_widget.domain.usecase.FetchLocationUseCase
import com.lgtm.daily_weather_widget.domain.usecase.FetchWeatherUseCase
import com.lgtm.daily_weather_widget.domain.vo.WeatherVO
import com.lgtm.daily_weather_widget.presentation.MainActivity
import com.lgtm.daily_weather_widget.utils.time.TimeProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val FORCE_APP_WIDGET_UPDATE = "com.lgtm.daily_weather_widget.APPWIDGET_UPDATE"

@AndroidEntryPoint
class DailyWeatherWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var fetchWeatherUseCase: FetchWeatherUseCase
    @Inject
    lateinit var fetchLocationUseCase: FetchLocationUseCase
    @Inject
    lateinit var timeProvider: TimeProvider

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == FORCE_APP_WIDGET_UPDATE || intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
            context?.let {
                updateAppWidget(it, appWidgetManager, id)
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
    ) {
        GlobalScope.launch {
            val location = fetchLocationUseCase()
            if (location.data == null) {
                return@launch
            }

            val now = timeProvider.getCurrentTimeMillis()
//            fetchWeatherUseCase(location.data.latitude, location.data.longitude, now / 1000).collect { weatherData ->
//                when (weatherData) {
//                    is Response.Success -> {
//                        weatherData.data?.let {
//                            onFetchWeatherSuccess(context, appWidgetManager, appWidgetId, it, now)
//                            Log.d("Doran", "now : ${timeProvider.getSimpleTimeFormat(now)}")
//                        }
//                    }
//                    else -> {
//                        return@collect
//                    }
//                }
//            }
        }
    }

    //TODO. add loading progressbar
    private fun onFetchWeatherSuccess(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        weatherData: WeatherVO,
        currentTimeInMillis: Long
    ) {
        val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
            .let { intent ->
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            }

        val refreshPendingIntent: PendingIntent = Intent(context, DailyWeatherWidgetProvider::class.java)
            .let { intent ->
                intent.action = FORCE_APP_WIDGET_UPDATE
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)
            }

        // TODO. Refactor ex.Factory
        val smallWidgetModel = WeatherWidgetUi(
            title = mapToWeatherTitle(weatherData.todayWeather?.temperature!!, weatherData.yesterdayTemperature),
            message = "${weatherData.yesterdayTemperature.roundToInt()}\u00B0C     >>     ${weatherData.todayWeather.temperature.roundToInt()}\u00B0C",
            mainImageRes = mapToWeatherMainImageRes(weatherData.todayWeather.temperature, weatherData.yesterdayTemperature),
            lastUpdateDate = timeProvider.getSimpleTimeFormat(currentTimeInMillis)
        )
        val smallView = RemoteViews(context.packageName, R.layout.daily_weather_widget_small).apply {
            setOnClickPendingIntent(R.id.root, pendingIntent)
            setOnClickPendingIntent(R.id.refresh, refreshPendingIntent)
            setTextViewText(R.id.weather_title, smallWidgetModel.title)
            setTextViewText(R.id.weather_temperature, smallWidgetModel.message)
            setTextViewText(R.id.last_update, smallWidgetModel.lastUpdateDate)
        }

        val wideWidgetModel = WeatherWidgetUi(
            title = mapToWeatherTitle(weatherData.todayWeather.temperature, weatherData.yesterdayTemperature),
            message = "${weatherData.yesterdayTemperature.roundToInt()}\u00B0C     >>     ${weatherData.todayWeather.temperature.roundToInt()}\u00B0C",
            mainImageRes = mapToWeatherMainImageRes(weatherData.todayWeather.temperature, weatherData.yesterdayTemperature),
            lastUpdateDate = timeProvider.getCurrentTimeInISO8601(currentTimeInMillis)
        )
        val wideView = RemoteViews(context.packageName, R.layout.daily_weather_widget_wide).apply {
            setOnClickPendingIntent(R.id.root, pendingIntent)
            setOnClickPendingIntent(R.id.refresh, refreshPendingIntent)
            setTextViewText(R.id.weather_title, wideWidgetModel.title)
            setTextViewText(R.id.weather_temperature, wideWidgetModel.message)
            setImageViewResource(R.id.weather_main_image, wideWidgetModel.mainImageRes)
            setTextViewText(R.id.last_update, smallWidgetModel.lastUpdateDate)
        }

        val viewMapping: Map<SizeF, RemoteViews> = mapOf(
            SizeF(150f, 100f) to smallView,
            SizeF(215f, 100f) to wideView,
        )

        val remoteViews = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            RemoteViews(viewMapping)
        } else {
            wideView
        }

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    private val THRESHOLD = 2.0

    private fun mapToWeatherTitle(todayTemp: Float, yesterdayTemp: Float): String {
        val dailyTempDiff = todayTemp - yesterdayTemp
        return if (-THRESHOLD <= dailyTempDiff && dailyTempDiff < THRESHOLD) {
            "어제와 비슷해요"
        } else if (dailyTempDiff < -THRESHOLD) {
            if (todayTemp < 10) "어제보다 시원해요"
            else "어제보다 추워요"
        } else {
            if (todayTemp < 20) "어제보다 따듯해요"
            else "어제보다 더워요"
        }

    }

    // TODO. Refactor
    private fun mapToWeatherMainImageRes(todayTemp: Float, yesterdayTemp: Float): Int {
        val dailyTempDiff = todayTemp - yesterdayTemp
        return if (-THRESHOLD <= dailyTempDiff && dailyTempDiff < THRESHOLD) {
            R.drawable.slowbro_flat
        } else if (dailyTempDiff < -THRESHOLD) {
            R.drawable.slowbro_decrease
        } else {
            R.drawable.slowbro_increase
        }
    }

}

data class WeatherWidgetUi(
    val title: String? = null,
    val message: String? = null,
    val mainImageRes: Int = -1,
    val lastUpdateDate: String? = null,
)