package com.projectambrosia.ambrosia.journal

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.ListItemJournalPromptBinding

class JournalPromptAdapter(private val journalPromptListener: JournalPromptListener) : ListAdapter<JournalPrompt, JournalPromptAdapter.ViewHolder>(JournalPromptDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, journalPromptListener)
    }

    fun addFreestyleAndSubmitList(list: List<JournalPrompt>?, application: Application) {
        val freeStylePrompt = JournalPrompt(application.resources.getString(R.string.freestyle_description))
        val items = when (list) {
            null -> listOf(freeStylePrompt)
            else -> list + listOf(freeStylePrompt)
        }
        submitList(items)
    }

    class ViewHolder private constructor(val binding: ListItemJournalPromptBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JournalPrompt, journalPromptListener: JournalPromptListener) {
            binding.prompt = item
            binding.clickListener = journalPromptListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemJournalPromptBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

data class JournalPrompt(val promptText: String, val taskId: Long? = null)

class JournalPromptDiffCallback : DiffUtil.ItemCallback<JournalPrompt>() {
    override fun areItemsTheSame(oldItem: JournalPrompt, newItem: JournalPrompt): Boolean {
        return oldItem.taskId == oldItem.taskId
    }

    override fun areContentsTheSame(oldItem: JournalPrompt, newItem: JournalPrompt): Boolean {
        return oldItem == newItem
    }
}

class JournalPromptListener(val clickListener: (prompt: JournalPrompt) -> Unit) {
    fun onClick(prompt: JournalPrompt) = clickListener(prompt)
}