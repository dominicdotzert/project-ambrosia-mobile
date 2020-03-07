package com.projectambrosia.ambrosia.tasks

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.databinding.ListItemTaskBinding
import com.projectambrosia.ambrosia.utilities.Tool
import com.projectambrosia.ambrosia.utilities.getHistoryDateString
import com.projectambrosia.ambrosia.views.DateViewHolder

private const val ITEM_VIEW_TYPE_TASK = 0
private const val ITEM_VIEW_TYPE_DATE = 1

class TaskAdapter(
    private val clickListener: TaskListener
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
                holder.bind(taskItem.task, clickListener)
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

    class TaskViewHolder private constructor(val binding: ListItemTaskBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task, clickListener: TaskListener) {
            binding.task = item
            binding.text = getTaskText(item)
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        private fun getTaskText(task: Task): Spanned {
            val taskText = when (task.tool) {
                Tool.JOURNAL -> context.resources.getString(R.string.task_item_journal, task.taskText)
                Tool.HS -> context.resources.getString(R.string.task_item_hunger_scale, task.taskText)
                else -> context.resources.getString(R.string.task_item_action, task.taskText)
            }

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(taskText, Html.FROM_HTML_MODE_COMPACT)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(taskText)
            }
        }

        companion object {
            fun from(parent: ViewGroup): TaskViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
                return TaskViewHolder(binding, parent.context)
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