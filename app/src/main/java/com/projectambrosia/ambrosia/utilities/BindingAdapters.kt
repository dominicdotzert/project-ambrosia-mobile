package com.projectambrosia.ambrosia.utilities

import androidx.databinding.BindingAdapter
import com.projectambrosia.ambrosia.ieas.IEASResultView

@BindingAdapter("percentage")
fun setResultsPercentage(view: IEASResultView, percentage: Int) {
    view.updatePercentage(percentage)
}