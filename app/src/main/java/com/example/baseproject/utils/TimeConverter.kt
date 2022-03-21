package com.example.baseproject.utils

//ms = (h*3600 + m*60 + s)*1000
fun convertToTimeString(ms: Long): String {
    val totalSecond = ms / 1000

    val hour = totalSecond / 3600
    val minute = (totalSecond % 3600) / 60
    val second = (totalSecond % 60)

    val hourStr = if (hour < 10) "0$hour" else hour.toString()
    val minuteStr = if (minute < 10) "0$minute" else minute.toString()
    val secondStr = if (second < 10) "0$second" else second.toString()

    return if (hour > 0) ("$hourStr : $minuteStr:$secondStr") else ("$minuteStr:$secondStr")
}