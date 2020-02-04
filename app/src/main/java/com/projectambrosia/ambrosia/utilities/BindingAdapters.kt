package com.projectambrosia.ambrosia.utilities

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.projectambrosia.ambrosia.views.IEASResultView

@BindingAdapter("percentage")
fun setResultsPercentage(view: IEASResultView, percentage: Int) {
    view.updatePercentage(percentage)
}

@BindingAdapter("journal_prompt_task", "prompt_text", "freestyle_text")
fun setJournalPromptHeading(view: TextView, taskId: Long?, prompt: String, freestyle: String) {
    view.text = if (taskId == null) freestyle else prompt
}