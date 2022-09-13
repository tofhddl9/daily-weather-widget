package com.lgtm.daily_weather_widget.setting

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR)
        val minute = now.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        val bundle = Bundle().apply {
            putInt("KEY_HOUR", hour)
            putInt("KEY_MINUTE", minute)
        }
        setFragmentResult("KEY_TIME_PICKER", bundle)
    }

}