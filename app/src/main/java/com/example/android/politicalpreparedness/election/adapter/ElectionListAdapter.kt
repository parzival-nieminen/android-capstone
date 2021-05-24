package com.example.android.politicalpreparedness.election.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemContentBinding
import com.example.android.politicalpreparedness.databinding.ItemTitleBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(
    private val headerTitle: String,
    private val onClickListener: (Election) -> Unit
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffCallback) {

    private val header = 0
    private val content = 1

    fun addHeaderAndSubmitList(list: List<Election>?) {
        val items = when (list) {
            null -> listOf(DataItem.Header)
            else -> listOf(DataItem.Header) + list.map { DataItem.ElectionItem(it) }
        }
        submitList(items)
    }


    companion object DiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            header -> ViewHolderElectionHeader.from(parent)
            content -> ViewHolderElectionBinding.from(parent)
            else -> throw RuntimeException("Unknown view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> header
            is DataItem.ElectionItem -> content
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderElectionHeader -> {
                holder.bind(headerTitle)
            }
            is ViewHolderElectionBinding -> {
                val item = (getItem(position) as DataItem.ElectionItem).election
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#F2F2F2"))
                } else {
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }

                holder.bind(item, onClickListener)
            }
        }
    }

    class ViewHolderElectionBinding(private var binding: ItemContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(election: Election, onClickListener: (Election) -> Unit) {
            binding.election = election
            binding.root.setOnClickListener { onClickListener(election) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolderElectionBinding {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContentBinding.inflate(layoutInflater, parent, false)
                return ViewHolderElectionBinding(binding)
            }
        }
    }

    class ViewHolderElectionHeader(private var binding: ItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.title = title
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolderElectionHeader {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTitleBinding.inflate(layoutInflater, parent, false)
                return ViewHolderElectionHeader(binding)
            }
        }
    }
}

sealed class DataItem {

    abstract val id: Int

    data class ElectionItem(val election: Election) : DataItem() {
        override val id = election.id
    }

    object Header : DataItem() {
        override val id = Int.MIN_VALUE
    }
}
