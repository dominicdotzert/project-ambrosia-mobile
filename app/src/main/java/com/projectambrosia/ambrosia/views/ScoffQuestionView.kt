package com.projectambrosia.ambrosia.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.projectambrosia.ambrosia.R
import kotlinx.android.synthetic.main.view_scoff_question.view.*

class ScoffQuestionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    val checked: Boolean
        get() = scoff_question_checkbox.isChecked

    init {
        LayoutInflater.from(context).inflate(R.layout.view_scoff_question, this, true)

        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.ScoffQuestionView, 0, 0)

            val question = attributes.getString(R.styleable.ScoffQuestionView_question)
            scoff_question_text.text = question

            attributes.recycle()
        }

        scoff_question_clickable_overlay.setOnClickListener{
            scoff_question_checkbox.toggle()
        }
    }
}