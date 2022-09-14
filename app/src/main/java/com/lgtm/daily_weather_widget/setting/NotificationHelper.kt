package com.lgtm.daily_weather_widget.setting

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.lgtm.daily_weather_widget.R
import com.lgtm.daily_weather_widget.presentation.MainActivity
import android.app.PendingIntent

private const val CHANNEL_ID = "CHANNEL_ID"
private const val CHANNEL_NAME = "CHANNEL_NAME"

class NotificationHelper(
    private val context: Context,
) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun getNotification(): Notification {
        createChannel()

        val intent = Intent(context, MainActivity::class.java)
        val notifyPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Daily Weather Caster")
            .setContentText("오늘의 날씨를 확인하세요")
            .setSmallIcon(R.drawable.ic_baseline_wb_sunny_24)
            .setContentIntent(notifyPendingIntent)
            .build()
    }

    private fun createChannel() {
        NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).also {
            notificationManager.createNotificationChannel(it)
        }
    }


}