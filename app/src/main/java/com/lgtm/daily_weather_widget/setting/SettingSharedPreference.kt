package com.lgtm.daily_weather_widget.setting

import android.content.Context
import android.content.SharedPreferences

class SettingSharedPreference(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, 0)

    var doesNotify: Boolean
        get() = prefs.getBoolean(KEY_PREF_NOTIFICATION_ON_OFF, false)
        set(value) = prefs.edit().putBoolean(KEY_PREF_NOTIFICATION_ON_OFF, value).apply()

    var notificationHour: Int
        get() = prefs.getInt(KEY_PREF_NOTIFICATION_HOUR, 8)
        set(value) = prefs.edit().putInt(KEY_PREF_NOTIFICATION_HOUR, value).apply()

    var notificationMinute: Int
        get() = prefs.getInt(KEY_PREF_NOTIFICATION_MINUTE, 0)
        set(value) = prefs.edit().putInt(KEY_PREF_NOTIFICATION_MINUTE, value).apply()

    companion object {
        const val PREF_NAME = "SETTING_PREFERENCE"

        // Notification
        const val KEY_PREF_NOTIFICATION_ON_OFF = "KEY_NOTIFICATION_ON_OFF"
        const val KEY_PREF_NOTIFICATION_HOUR = "KEY_NOTIFICATION_HOUR"
        const val KEY_PREF_NOTIFICATION_MINUTE = "KEY_NOTIFICATION_MINUTE"

    }
}