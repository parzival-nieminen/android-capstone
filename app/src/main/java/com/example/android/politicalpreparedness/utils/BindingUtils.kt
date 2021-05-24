package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*

/**
These bindings adapters are taken from the previous projects
 **/

@BindingAdapter("displayProgressBar")
fun bindDisplayProgressBar(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("listData")
fun bindListData(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    data?.let { adapter.addHeaderAndSubmitList(it) }
    recyclerView.scrollToPosition(0)
}

@BindingAdapter("noData")
fun bindNoData(view: View, data: List<Election>?) {
    view.visibility = if (data?.isEmpty() == true) View.VISIBLE else View.GONE
}

@BindingAdapter("DateFormat")
fun bindDateFormat(textView: TextView, date: Date?) {
    textView.text = ""
    if (date != null) {
        textView.text = date.toFormatString()
    }
}

@BindingAdapter("displayViewNotNull")
fun bindDisplayViewNotNull(view: View, it: Any?) {
    view.visibility = if (it == null) View.GONE else View.VISIBLE
}

@BindingAdapter("buttonText")
fun bindButtonText(button: Button, isFollow: Boolean) {
    if (isFollow)
        button.setText(R.string.button_unfollow_election)
    else
        button.setText(R.string.button_follow_election)
}
