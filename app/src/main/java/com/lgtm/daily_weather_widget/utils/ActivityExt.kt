package com.lgtm.daily_weather_widget.utils

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnackBar(msg:String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this.findViewById(android.R.id.content), msg, duration).show()
}
