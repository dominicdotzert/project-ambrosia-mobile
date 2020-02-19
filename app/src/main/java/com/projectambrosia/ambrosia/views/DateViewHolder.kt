package com.projectambrosia.ambrosia.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectambrosia.ambrosia.databinding.ListItemHistoryDateBinding

class DateViewHolder private constructor(val binding: ListItemHistoryDateBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(date: String) {
        binding.date = date
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): DateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemHistoryDateBinding.inflate(layoutInflater, parent, false)
            return DateViewHolder(binding)
        }
    }
}