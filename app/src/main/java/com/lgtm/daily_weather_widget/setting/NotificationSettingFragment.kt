package com.lgtm.daily_weather_widget.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.lgtm.daily_weather_widget.R
import com.lgtm.daily_weather_widget.utils.time.to2Str
import java.util.*

class NotificationSettingFragment : PreferenceFragmentCompat() {

    private val settingPreference by lazy { SettingSharedPreference(requireContext()) }

    private lateinit var notificationAlarmManager: NotificationAlarmManager

    private var notificationOnOff: SwitchPreference? = null
    private var notificationTime: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_setting_preference, rootKey)

        initNotificationOnOff()

        initNotificationTime()

        setNotification()

        setFragmentResults()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        notificationAlarmManager = NotificationAlarmManager(context)
    }

    private fun initNotificationOnOff() {
        notificationOnOff = findPreference("notification_onoff")
        notificationOnOff?.isChecked = settingPreference.doesNotify
        notificationOnOff?.setOnPreferenceChangeListener { _, isChecked ->
            settingPreference.doesNotify = isChecked as Boolean
            if (isChecked) {
                setNotification()
            } else {
                cancelNotification()
            }
            true
        }
    }

    private fun initNotificationTime() {
        notificationTime = findPreference("notification_time")
        showNotificationTime()
        notificationTime?.setOnPreferenceClickListener {
            moveToTimePickerFragment()
            true
        }
    }

    private fun setFragmentResults() {
        setFragmentResultListener("KEY_TIME_PICKER") { _, result ->
            val hour = result.getInt("KEY_HOUR")
            val minute = result.getInt("KEY_MINUTE")

            settingPreference.notificationHour = hour
            settingPreference.notificationMinute = minute

            showNotificationTime()

            cancelNotification()
            setNotification()
        }
    }

    private fun showNotificationTime() {
        notificationTime?.summary = "${settingPreference.notificationHour.to2Str()} : ${settingPreference.notificationMinute.to2Str()}"
    }

    private fun moveToTimePickerFragment() {
        val action = NotificationSettingFragmentDirections.actionNotificationSettingFragmentToTimePickerFragment()
        findNavController().navigate(action)
    }

    private fun setNotification() {
        if (notificationOnOff?.isChecked == true) {
            val hour = settingPreference.notificationHour
            val minute = settingPreference.notificationMinute

            notificationAlarmManager.setNotificationAlarm(hour, minute)
        }
    }

    private fun cancelNotification() {
        notificationAlarmManager.cancelNotificationAlarm()
    }

}