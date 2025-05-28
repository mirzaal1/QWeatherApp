package com.mirzaali.qweatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Long.toFormattedTime(): String {
    val date = Date(this * 1000)
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return format.format(date)
}
