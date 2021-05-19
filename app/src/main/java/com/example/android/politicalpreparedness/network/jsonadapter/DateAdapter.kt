package com.example.android.politicalpreparedness.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter {
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    @FromJson
    fun dateFromJson(date: String): Date = format.parse(date)!!

    @ToJson
    fun dateToJson(date: Date): String = format.format(date)
}
