package com.lgtm.daily_weather_widget.setting

import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.lgtm.daily_weather_widget.R
import com.lgtm.daily_weather_widget.utils.time.to2Str

class NotificationSettingFragment : PreferenceFragmentCompat() {

    private val settingPreference by lazy { SettingSharedPreference(requireContext()) }

    private var notificationOnOff: SwitchPreference? = null
    private var notificationTime: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_setting_preference, rootKey)

        initNotificationOnOff()

        initNotificationTime()

        setFragmentResults()
    }

    private fun initNotificationOnOff() {
        notificationOnOff = findPreference("notification_onoff")
        notificationOnOff?.isChecked = settingPreference.doesNotify
        notificationOnOff?.setOnPreferenceChangeListener { _, option ->
            settingPreference.doesNotify = option as Boolean
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
        }
    }

    private fun showNotificationTime() {
        notificationTime?.summary = "${settingPreference.notificationHour.to2Str()} : ${settingPreference.notificationMinute.to2Str()}"
    }

    private fun moveToTimePickerFragment() {
        val action = NotificationSettingFragmentDirections.actionNotificationSettingFragmentToTimePickerFragment()
        findNavController().navigate(action)
    }
}