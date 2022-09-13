package com.lgtm.daily_weather_widget.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.lgtm.daily_weather_widget.R

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey)

        findPreference<Preference>("notification")?.setOnPreferenceClickListener {
            moveToNotificationPreferenceFragment()
            true
        }
    }

    private fun moveToNotificationPreferenceFragment() {
        val action = SettingFragmentDirections.actionSettingFragmentToNotificationSettingFragment()
        findNavController().navigate(action)
    }
}