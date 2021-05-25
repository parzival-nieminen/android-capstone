package com.example.android.politicalpreparedness.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}
