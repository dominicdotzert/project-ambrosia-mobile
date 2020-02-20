package com.projectambrosia.ambrosia.utilities

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.views.IEASResultView
import java.util.*

// IEAS
@BindingAdapter("percentage")
fun setResultsPercentage(view: IEASResultView, percentage: Int) {
    view.updatePercentage(percentage)
}

// Journal
@BindingAdapter("journal_prompt_task", "prompt_text", "freestyle_text")
fun setJournalPromptHeading(view: TextView, taskId: Long?, prompt: String, freestyle: String) {
    view.text = if (taskId == null) freestyle else prompt
}

@BindingAdapter("journal_entry_history_placeholder_visibility")
fun setJournalHistoryPlaceholderVisibility(view: ViewGroup, prompts: List<JournalEntry>?) {
    view.visibility = if (prompts == null || prompts.isEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("journal_entry_history_placeholder_visibility_inverted")
fun setJournalHistoryPlaceholderVisibilityInverted(view: ViewGroup, prompts: List<JournalEntry>?) {
    view.visibility = if (prompts == null || prompts.isEmpty()) View.GONE else View.VISIBLE
}

// Tasks
@BindingAdapter("task_list_placeholder_visibility")
fun setTaskListPlaceholderVisibility(view: ViewGroup, tasks: List<Task>?) {
    view.visibility = if (tasks == null || tasks.isEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("task_list_placeholder_visibility_inverted")
fun setTaskListPlaceholderVisibilityInverted(view: ViewGroup, tasks: List<Task>?) {
    view.visibility = if (tasks == null || tasks.isEmpty()) View.GONE else View.VISIBLE
}

// Hunger Scale
@BindingAdapter("hs_history_placeholder_visibility")
fun setHSHistoryPlaceholderVisibility(view: ViewGroup, history: List<HSEntry>?) {
    view.visibility = if (history == null || history.isEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("hs_history_placeholder_visibility_inverted")
fun setHSHistoryPlaceholderVisibilityInverted(view: ViewGroup, history: List<HSEntry>?) {
    view.visibility = if (history == null || history.isEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("hs_visible_if_entry_not_null")
fun setHSVisibilityIfEntryNotNull(view: View, entry: HSEntry?) {
    view.visibility = if (entry != null) View.VISIBLE else View.GONE
}

// Generic
@BindingAdapter("visibility_adapter_inverted", "set_invisible")
fun setViewVisibility(view: View, isVisible: Boolean, setInvisible: Boolean = false) {
    if (!isVisible)
        view.visibility = View.VISIBLE
    else if (setInvisible)
        view.visibility = View.INVISIBLE
    else
        view.visibility = View.GONE
}

@BindingAdapter("children_enabled_if_today")
fun setChildrenEnabledIfToday(view: ViewGroup, calendar: Calendar) {
    val isToday = calendar.isToday()
    for (child in view.children) {
        child.isEnabled = isToday
    }
}

@BindingAdapter("entry_time")
fun formatJournalHistoryDate(view: TextView, entryDate: Calendar?) {
    entryDate?.let {
        view.text = formatTime(entryDate)
    }
}

@BindingAdapter("enabled_if_int_not_null")
fun setIsEnabledIfNotNull(view: View, int: Int?) {
    view.isEnabled = int != null
}