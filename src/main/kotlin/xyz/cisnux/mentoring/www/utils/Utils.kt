package xyz.cisnux.mentoring.www.utils

import java.util.*

fun Long.addDuration(duration: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.add(Calendar.MINUTE, duration)
    return calendar.time.time
}