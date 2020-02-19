package com.projectambrosia.ambrosia.journal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.databinding.ListItemJournalHistoryBinding
import com.projectambrosia.ambrosia.utilities.getHistoryDateString
import com.projectambrosia.ambrosia.views.DateViewHolder

private const val ITEM_VIEW_TYPE_JOURNAL_ENTRY = 0
private const val ITEM_VIEW_TYPE_DATE = 1

class JournalHistoryAdapter : ListAdapter<JournalHistoryItem, RecyclerView.ViewHolder>(JournalHistoryItemDiffCallback()){

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is JournalHistoryItem.EntryItem -> ITEM_VIEW_TYPE_JOURNAL_ENTRY
            is JournalHistoryItem.DateItem -> ITEM_VIEW_TYPE_DATE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_JOURNAL_ENTRY -> JournalEntryViewHolder.from(parent)
            ITEM_VIEW_TYPE_DATE -> DateViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is JournalEntryViewHolder -> {
                val entryItem = getItem(position) as JournalHistoryItem.EntryItem
                holder.bind(entryItem.entry)
            }
            is DateViewHolder -> {
                val dateItem = getItem(position) as JournalHistoryItem.DateItem
                holder.bind(dateItem.date)
            }
        }
    }

    fun addDatesAndSubmitList(entryList: List<JournalEntry>?, multipleDates: Boolean) {
        if (!multipleDates) {
            submitList(entryList?.map { JournalHistoryItem.EntryItem(it) })
            return
        }

        val groupedEntryList = entryList?.groupBy { entry -> entry.entryDate.getHistoryDateString() }
        val list = mutableListOf<JournalHistoryItem>()
        groupedEntryList?.forEach { entryGrouping ->
            if (entryGrouping.key != "Today")
                list.add(JournalHistoryItem.DateItem(entryGrouping.key))
            list.addAll(entryGrouping.value.map { JournalHistoryItem.EntryItem(it) })
        }
        submitList(list)
    }

    class JournalEntryViewHolder private constructor(val binding: ListItemJournalHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JournalEntry) {
            binding.entry = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : JournalEntryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemJournalHistoryBinding.inflate(layoutInflater, parent, false)
                return JournalEntryViewHolder(binding)
            }
        }
    }
}

sealed class JournalHistoryItem {
    abstract val id: String

    data class EntryItem(val entry: JournalEntry) : JournalHistoryItem() {
        override val id = entry.entryId.toString()
    }

    data class DateItem(val date: String) : JournalHistoryItem() {
        override val id = date
    }
}

class JournalHistoryItemDiffCallback : DiffUtil.ItemCallback<JournalHistoryItem>() {
    override fun areItemsTheSame(oldItem: JournalHistoryItem, newItem: JournalHistoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: JournalHistoryItem, newItem: JournalHistoryItem): Boolean {
        return oldItem == newItem
    }
}