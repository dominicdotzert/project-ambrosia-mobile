package com.projectambrosia.ambrosia.utilities

import android.content.Context
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

suspend fun prepopulateDatabase(context: Context) {
    withContext(Dispatchers.IO) {
        val db = AmbrosiaDatabase.getInstance(context)

        db.userDao.insert(
            User(1, "test@test.com", "Jane Doe", 1, "I want to live my life free from the control of food and love my body for the way it is.", Calendar.getInstance())
        )

        db.taskDao.insert(
            Task(1, 1, Calendar.getInstance(), "Today - Journal", 1, Tool.JOURNAL, 1),
            Task(2, 1, Calendar.getInstance(), "Today - HS", 1, Tool.HS, 1),
            Task(3, 1, Calendar.getInstance(), "Today - IEAS", 1, Tool.IEAS, 1),
            Task(4, 1, Calendar.getInstance(), "Today - Other", 1, Tool.OTHER, 1),
            Task(5, 1, Calendar.getInstance(), "Today - Other - Completed", 1, Tool.OTHER, 1, true),

            Task(6, 1, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000) }, "Yesterday - Other - Completed", 1, Tool.OTHER, 1, true),
            Task(7, 1, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*2) }, "2 days ago - Other - Completed", 1, Tool.OTHER, 1, true),
            Task(8, 1, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*3) }, "3 days ago - Other - Completed", 1, Tool.OTHER, 1, true),
            Task(9, 1, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*4) }, "4 days ago - Other - Completed", 1, Tool.OTHER, 1, true)
        )
    }
}