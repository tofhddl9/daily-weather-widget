package com.lgtm.daily_weather_widget.setting

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        with(NotificationHelper(context)) {
            notificationManager.notify(1, getNotification())
        }
    }
}