package com.projectambrosia.ambrosia.utilities

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.journal.JournalPrompt
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

@BindingAdapter("journal_entry_history_selector_visibility")
fun setJournalHistorySelectorVisibility(view: ViewGroup, prompts: List<JournalEntry>?) {
    view.visibility =
        if (prompts == null || prompts.isEmpty() || prompts.none { prompt -> !prompt.entryDate.isToday() })
            View.GONE
        else
            View.VISIBLE
}

@BindingAdapter("journal_task_label_visibility")
fun setJournalTaskLabelVisibility(view: View, prompt: JournalPrompt?) {
    view.visibility = if (prompt?.taskId == null) View.GONE else View.VISIBLE
}

// Tasks
@BindingAdapter("task_list_placeholder_visibility")
fun setTaskListPlaceholderVisibility(view: View, tasks: List<Task>?) {
    view.visibility = if (tasks == null || tasks.isEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("task_list_placeholder_visibility_inverted")
fun setTaskListPlaceholderVisibilityInverted(view: View, tasks: List<Task>?) {
    view.visibility = if (tasks == null || tasks.isEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("task_list_selector_visibility")
fun setTaskListSelectorVisibility(view: ViewGroup, tasks: List<Task>?) {
    view.visibility =
        if (tasks == null || tasks.isEmpty() || tasks.none { task -> !task.timestamp.isToday() })
            View.GONE
        else
            View.VISIBLE
}

@BindingAdapter("task_icon_src")
fun setTaskIcon(imageView: ImageView, task: Task?) {
    var imageResource = R.drawable.ic_check

    task?.tool.let {
        if (it == Tool.JOURNAL) imageResource = R.drawable.ic_journal_black_24dp
        else if (it == Tool.HS) imageResource = R.drawable.ic_hunger_scale_black_24dp
    }

    imageView.setImageResource(imageResource)
}

@BindingAdapter("task_chevron_visibility")
fun setTaskChevronVisibility(chevron: ImageView, task: Task?) {
    chevron.visibility = if (task?.isClickable() == true) View.VISIBLE else View.GONE
}

@BindingAdapter("disable_children_if_in_history")
fun setChildrenEnabledIfTaskIsClickable(view: ViewGroup, task: Task?) {
    val tasksEnabled = task?.isClickable() ?: true
    for (child in view.children) {
        child.isEnabled = tasksEnabled
    }
}

// Hunger Scale
@BindingAdapter("hs_history_placeholder_visibility")
fun setHSHistoryPlaceholderVisibility(view: ViewGroup, history: List<HSEntry>?) {
    view.visibility = if (history == null || history.isEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("hs_history_selector_visibility")
fun setHSHistorySelectorVisibility(view: ViewGroup, history: List<HSEntry>?) {
    view.visibility =
        if (history == null || history.isEmpty() || history.none { entry -> !entry.entryDate.isToday() })
            View.GONE
        else
            View.VISIBLE
}

@BindingAdapter("hs_visible_if_entry_not_null")
fun setHSVisibilityIfEntryNotNull(view: View, entry: HSEntry?) {
    view.visibility = if (entry != null) View.VISIBLE else View.GONE
}

@BindingAdapter("hs_header_text")
fun setHSHeaderText(view: TextView, task: Task?) {
    val textId = if (task == null) R.string.rate_your_hunger else R.string.task
    view.setText(textId)
}

@BindingAdapter("hs_task_text_visibility")
fun setHSTaskTextVisibility(view: View, task: Task?) {
    view.visibility = if (task == null) View.GONE else View.VISIBLE
}

// Generic
@BindingAdapter("visibility_adapter", "set_invisible")
fun setViewVisibility(view: View, bool: Boolean, setInvisible: Boolean = false) {
    view.visibility = when {
        bool -> View.VISIBLE
        setInvisible -> View.INVISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("visibility_adapter_inverted", "set_invisible")
fun setViewVisibilityInverted(view: View, bool: Boolean, setInvisible: Boolean = false) {
    view.visibility = when {
        !bool -> View.VISIBLE
        setInvisible -> View.INVISIBLE
        else -> View.GONE
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

@BindingAdapter("enabled_inverted")
fun setEnabledInverted(view: View, bool: Boolean) {
    view.isEnabled = !bool
}