package com.projectambrosia.ambrosia.hungerscale

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.databinding.ListItemHsEntryBinding
import com.projectambrosia.ambrosia.utilities.getHistoryDateString
import com.projectambrosia.ambrosia.views.DateViewHolder

private const val ITEM_VIEW_TYPE_HS_ENTRY = 0
private const val ITEM_VIEW_TYPE_DATE = 1

class HungerScaleHistoryAdapter
    : ListAdapter<HSAdapterItem, RecyclerView.ViewHolder>(HSAdapterItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HSAdapterItem.HSEntryItem -> ITEM_VIEW_TYPE_HS_ENTRY
            is HSAdapterItem.DateItem -> ITEM_VIEW_TYPE_DATE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HS_ENTRY -> HungerScaleViewHolder.from(parent)
            ITEM_VIEW_TYPE_DATE -> DateViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HungerScaleViewHolder -> {
                val entryItem = getItem(position) as HSAdapterItem.HSEntryItem
                holder.bind(entryItem.entry)
            }
            is DateViewHolder -> {
                val dateItem = getItem(position) as HSAdapterItem.DateItem
                holder.bind(dateItem.date)
            }
        }
    }

    fun addDatesAndSubmitList(entryList: List<HSEntry>?, multipleDates: Boolean) {
        if (!multipleDates) {
            submitList(entryList?.map { HSAdapterItem.HSEntryItem(it) })
            return
        }

        val groupedEntryList = entryList?.groupBy { entry -> entry.entryDate.getHistoryDateString() }
        val list = mutableListOf<HSAdapterItem>()
        groupedEntryList?.forEach { entryGrouping ->
            if (entryGrouping.key != "Today")
                list.add(HSAdapterItem.DateItem(entryGrouping.key))
            list.addAll(entryGrouping.value.map { HSAdapterItem.HSEntryItem(it) })
        }
        submitList(list)
    }

    class HungerScaleViewHolder private constructor(val binding: ListItemHsEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HSEntry) {
            binding.entry = item
            binding.executePendingBindings()
        }

        companion object {
            fun from (parent: ViewGroup): HungerScaleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemHsEntryBinding.inflate(layoutInflater, parent, false)
                return HungerScaleViewHolder(binding)
            }
        }
    }
}

sealed class HSAdapterItem {
    abstract val id: String

    data class HSEntryItem(val entry: HSEntry) : HSAdapterItem() {
        override val id = entry.hsEntryId.toString()
    }

    data class DateItem(val date: String) : HSAdapterItem() {
        override val id = date
    }
}

class HSAdapterItemDiffCallback : DiffUtil.ItemCallback<HSAdapterItem>() {
    override fun areItemsTheSame(oldItem: HSAdapterItem, newItem: HSAdapterItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: HSAdapterItem, newItem: HSAdapterItem): Boolean {
        return oldItem == newItem
    }
}
