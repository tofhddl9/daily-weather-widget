package com.lgtm.daily_weather_widget.setting

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, p1: Intent?) {
        val settingPreference = SettingSharedPreference(context)
        val notificationAlarmManager = NotificationAlarmManager(context)

        if(settingPreference.doesNotify) {
            notificationAlarmManager.cancelNotificationAlarm()
            notificationAlarmManager.setNotificationAlarm(
                settingPreference.notificationHour,
                settingPreference.notificationMinute
            )
        }
    }
}