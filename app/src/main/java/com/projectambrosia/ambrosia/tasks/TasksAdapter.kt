package com.projectambrosia.ambrosia.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectambrosia.ambrosia.data.Task
import com.projectambrosia.ambrosia.databinding.ListItemTaskBinding

class TaskAdapter(private val taskListener: TaskListener) : ListAdapter<Task, TaskAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, taskListener)
    }

    class ViewHolder private constructor(val binding: ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task, taskListener: TaskListener) {
            binding.task = item
            binding.clickListener = taskListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}

class TaskListener(val clickListener: (task: Task?) -> Unit) {
    fun onClick(task: Task?) = clickListener(task)
}