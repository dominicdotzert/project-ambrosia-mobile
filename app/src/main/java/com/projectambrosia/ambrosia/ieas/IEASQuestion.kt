package com.projectambrosia.ambrosia.ieas

import android.content.Context
import com.projectambrosia.ambrosia.R

class IEASQuestion private constructor(val question: String, val questionSet: Int, var value: Boolean = false) {
    companion object {
        fun get(context: Context): List<IEASQuestion> {
            return listOf(
                IEASQuestion(context.resources.getString(R.string.ieas_question_1), 1),
                IEASQuestion(context.resources.getString(R.string.ieas_question_2), 1),
                IEASQuestion(context.resources.getString(R.string.ieas_question_3), 1),
                IEASQuestion(context.resources.getString(R.string.ieas_question_4), 1),
                IEASQuestion(context.resources.getString(R.string.ieas_question_5), 1),
                IEASQuestion(context.resources.getString(R.string.ieas_question_6), 1),
                IEASQuestion(context.resources.getString(R.string.ieas_question_7), 2),
                IEASQuestion(context.resources.getString(R.string.ieas_question_8), 2),
                IEASQuestion(context.resources.getString(R.string.ieas_question_9), 2),
                IEASQuestion(context.resources.getString(R.string.ieas_question_10), 2),
                IEASQuestion(context.resources.getString(R.string.ieas_question_11), 2),
                IEASQuestion(context.resources.getString(R.string.ieas_question_12), 2),
                IEASQuestion(context.resources.getString(R.string.ieas_question_13), 2),
                IEASQuestion(context.resources.getString(R.string.ieas_question_14), 2),
                IEASQuestion(context.resources.getString(R.string.ieas_question_15), 3),
                IEASQuestion(context.resources.getString(R.string.ieas_question_16), 3),
                IEASQuestion(context.resources.getString(R.string.ieas_question_17), 3),
                IEASQuestion(context.resources.getString(R.string.ieas_question_18), 3),
                IEASQuestion(context.resources.getString(R.string.ieas_question_19), 3),
                IEASQuestion(context.resources.getString(R.string.ieas_question_20), 3),
                IEASQuestion(context.resources.getString(R.string.ieas_question_21), 4),
                IEASQuestion(context.resources.getString(R.string.ieas_question_22), 4),
                IEASQuestion(context.resources.getString(R.string.ieas_question_23), 4)
            )
        }
    }
}