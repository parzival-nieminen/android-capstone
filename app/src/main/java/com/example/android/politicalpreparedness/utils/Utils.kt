package com.example.android.politicalpreparedness.utils

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun Date.toFormatString(): String {
    val format = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault())
    return format.format(this)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}
