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

/**
 * Turns a yyyy-MM-dd date back into epoch millis at UTC midnight, which is
 * what the date range picker needs to show an already-chosen date.
 *
 * @param date a date such as 2026-11-10
 * @return the date as epoch millis, or null if [text] is blank or not a real date
 */
fun parseDate(date: String): Long? {
    if (date.isBlank()) return null
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    format.isLenient = false
    return runCatching { format.parse(date)?.time }.getOrNull()
}