package com.lgtm.daily_weather_widget.utils.time

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import java.time.Clock
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SystemTimeProvider (
    private val clock: Clock = Clock.systemDefaultZone(),
    private val zoneId: ZoneId? = ZoneId.systemDefault(),
    locale: Locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0),
) : TimeProvider {

    private val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .withLocale(locale)
        .withZone(zoneId)

    private val now: Long
        get() = clock.millis()

    private fun zdt(dt: Long = now): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(dt), zoneId)
    }

    override fun getCurrentTimeMillis(): Long {
        return now
    }

    override fun getCurrentTimeInISO8601(dt: Long): String {
        return zdt(dt).format(format)
    }

    override fun getMonth(dt: Long): Int {
        return zdt(dt).monthValue
    }

    override fun getDayOfMonth(dt: Long): Int {
        return zdt(dt).dayOfMonth
    }

    override fun getDayWeek(dt: Long): DayOfWeek {
        return zdt(dt).dayOfWeek
    }

    override fun getHour(dt: Long): Int {
        return zdt(dt).hour
    }

    override fun isToday(dt: Long): Boolean {
        return zdt(dt).toLocalDate().equals(LocalDate.now(zoneId))
    }

    override fun isNight(dt: Long): Boolean {
        val hour = getHour(dt)
        return hour >= 20 || hour <= 6
    }

}
