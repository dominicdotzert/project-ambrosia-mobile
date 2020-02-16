package com.projectambrosia.ambrosia.utilities

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.views.IEASResultView
import java.util.*

@BindingAdapter("percentage")
fun setResultsPercentage(view: IEASResultView, percentage: Int) {
    view.updatePercentage(percentage)
}

@BindingAdapter("journal_prompt_task", "prompt_text", "freestyle_text")
fun setJournalPromptHeading(view: TextView, taskId: Long?, prompt: String, freestyle: String) {
    view.text = if (taskId == null) freestyle else prompt
}

@BindingAdapter("journal_entry_history")
fun setJournalHistoryPlaceholderVisibility(view: CardView, prompts: List<JournalEntry>?) {
    view.visibility = if (prompts == null || prompts.isEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("journal_entry_date")
fun formatJournalHistoryDate(view: TextView, entryDate: Calendar) {
    view.text = formatJournalEntryDate(entryDate)
}

@BindingAdapter("visibility_adapter_inverted", "set_invisible")
fun setViewVisibility(view: View, isVisible: Boolean, setInvisible: Boolean = false) {
    if (!isVisible)
        view.visibility = View.VISIBLE
    else if (setInvisible)
        view.visibility = View.INVISIBLE
    else
        view.visibility = View.GONE
}