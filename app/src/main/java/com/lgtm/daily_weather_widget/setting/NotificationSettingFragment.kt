package com.lgtm.daily_weather_widget.setting

import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.lgtm.daily_weather_widget.R
import com.lgtm.daily_weather_widget.utils.time.to2Str

class NotificationSettingFragment : PreferenceFragmentCompat() {

    private val settingPreference by lazy { SettingSharedPreference(requireContext()) }

    private var notificationTime: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_setting_preference, rootKey)

        findPreference<Preference>("notification_onoff")?.setOnPreferenceChangeListener { _, option ->
            true
        }

        notificationTime = findPreference("notification_time")
        showNotificationTime()
        notificationTime?.setOnPreferenceClickListener {
            moveToTimePickerFragment()
            true
        }

        setFragmentResults()
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