package com.lgtm.daily_weather_widget.utils.time

import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun DayOfWeek.toKorean(): String {
    return when (this) {
        DayOfWeek.SUNDAY -> "일"
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
    }
}

fun hourToStringFormat(hourIn24: Int): String {
    return when {
        hourIn24 == 12 -> {
            "오후 12시"
        }
        hourIn24 == 0 -> {
            "오전 0시"
        }
        hourIn24 > 12 -> {
            "오후 ${hourIn24 - 12}시"
        }
        else -> {
            "오전 ${hourIn24}시"
        }
    }
}

fun timeToSimpleFormat(dt: Long, pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(dt), ZoneId.systemDefault()).format(formatter)
}