package com.projectambrosia.ambrosia.tasks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.databinding.ListItemHistoryDateBinding
import com.projectambrosia.ambrosia.databinding.ListItemTaskBinding
import com.projectambrosia.ambrosia.utilities.getHistoryDateString

private const val ITEM_VIEW_TYPE_TASK = 0
private const val ITEM_VIEW_TYPE_DATE = 1

class TaskAdapter(
    private val taskListener: TaskListener
) : ListAdapter<TaskAdapterItem, RecyclerView.ViewHolder>(TaskAdapterItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TaskAdapterItem.TaskItem ->ITEM_VIEW_TYPE_TASK
            is TaskAdapterItem.DateItem ->ITEM_VIEW_TYPE_DATE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_TASK -> TaskViewHolder.from(parent)
            ITEM_VIEW_TYPE_DATE -> DateViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                val taskItem = getItem(position) as TaskAdapterItem.TaskItem
                holder.bind(taskItem.task, taskListener)
            }
            is DateViewHolder -> {
                val dateItem = getItem(position) as TaskAdapterItem.DateItem
                holder.bind(dateItem.date)
            }
        }
    }

    fun addDatesAndSubmitList(taskList: List<Task>?, multipleDates: Boolean) {
        if (!multipleDates) {
            submitList(taskList?.map { TaskAdapterItem.TaskItem(it) })
            return
        }

        val groupedTaskList = taskList?.groupBy { task -> task.timestamp.getHistoryDateString() }
        val list = mutableListOf<TaskAdapterItem>()
        groupedTaskList?.forEach { taskGrouping ->
            if (taskGrouping.key != "Today")
                list.add(TaskAdapterItem.DateItem(taskGrouping.key))
            list.addAll(taskGrouping.value.map { TaskAdapterItem.TaskItem(it) })
        }
        submitList(list)
    }

    class TaskViewHolder private constructor(val binding: ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task, taskListener: TaskListener) {
            binding.task = item
            binding.clickListener = taskListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TaskViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
                return TaskViewHolder(binding)
            }
        }
    }

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
}

sealed class TaskAdapterItem {
    abstract val id: String

    data class TaskItem(val task: Task) : TaskAdapterItem() {
        override val id = task.taskId.toString()
    }

    data class DateItem(val date: String) : TaskAdapterItem() {
        override val id = date
    }
}

class TaskAdapterItemDiffCallback : DiffUtil.ItemCallback<TaskAdapterItem>() {
    override fun areItemsTheSame(oldItem: TaskAdapterItem, newItem: TaskAdapterItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TaskAdapterItem, newItem: TaskAdapterItem): Boolean {
        return oldItem == newItem
    }
}

class TaskListener(val clickListener: (task: Task?) -> Unit) {
    fun onClick(task: Task?) = clickListener(task)
}