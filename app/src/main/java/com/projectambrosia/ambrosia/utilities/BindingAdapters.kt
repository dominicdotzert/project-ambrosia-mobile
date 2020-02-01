package com.projectambrosia.ambrosia.utilities

import androidx.databinding.BindingAdapter
import com.projectambrosia.ambrosia.views.IEASResultView

@BindingAdapter("percentage")
fun setResultsPercentage(view: IEASResultView, percentage: Int) {
    view.updatePercentage(percentage)
}