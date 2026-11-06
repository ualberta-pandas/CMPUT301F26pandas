package com.example.campsiteapp.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Formats a date from the date range picker as yyyy-MM-dd.
 *
 * @param millis epoch millis at UTC midnight
 * @return the date as text, example: 2026-11-10
 */
fun formatDate(millis: Long): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(Date(millis))
}