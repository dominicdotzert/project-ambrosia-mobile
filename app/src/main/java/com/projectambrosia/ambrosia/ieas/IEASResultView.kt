package com.projectambrosia.ambrosia.ieas

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.projectambrosia.ambrosia.R
import kotlinx.android.synthetic.main.view_ieas_result.view.*

class IEASResultView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CardView(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_ieas_result, this, true)

        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.IEASResultView, 0, 0)

            val category = attributes.getString(R.styleable.IEASResultView_category)
            ieas_result_category.text = category

            attributes.recycle()
        }
    }

    fun updatePercentage(percentage: Int) {
        val percentageHtmlString = context.resources.getString(R.string.ieas_percentage, percentage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ieas_result_percentage.text = Html.fromHtml(percentageHtmlString, Html.FROM_HTML_MODE_COMPACT)
        } else {
            ieas_result_percentage.text = Html.fromHtml(percentageHtmlString)
        }
    }
}