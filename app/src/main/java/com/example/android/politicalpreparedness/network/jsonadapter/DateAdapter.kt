package com.example.android.politicalpreparedness.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeParseException
import java.util.*

class DateAdapter {
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @ToJson
    fun dateToJson(date: Date?): String = try {
        format.format(date!!)
    } catch (parseException: ParseException) {
        Timber.e(parseException)
        ""
    }

    @FromJson
    fun dateFromJson(dateStr: String): Date? = try {
        format.parse(dateStr)
    } catch (dateTimeParseException: DateTimeParseException) {
        Timber.e(dateTimeParseException)
        Date()
    }
}
