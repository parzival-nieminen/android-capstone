package com.example.android.politicalpreparedness.utils

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun Date.toFormatString(): String {
    val format = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault())
    return format.format(this)
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}
