package com.projectambrosia.ambrosia.utilities

import android.content.Context
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.Task
import com.projectambrosia.ambrosia.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

suspend fun prepopulateDatabase(context: Context) {
    withContext(Dispatchers.IO) {
        val db = AmbrosiaDatabase.getInstance(context)

        db.userDao.insert(
            User(1, "test@test.com", "NAME", 1, "MOTIVATION")
        )

        db.taskDao.insert(
            Task(1, 1, Calendar.getInstance(), "Go to the journal", 1, Tool.JOURNAL, 1),
            Task(2, 1, Calendar.getInstance(), "Go to the hunger scale", 1, Tool.HS, 1),
            Task(3, 1, Calendar.getInstance(), "Go to the IEAS", 1, Tool.IEAS, 1),
            Task(4, 1, Calendar.getInstance(), "Do something unrelated to our tools and also that is really long and will require text to wrap", 1, Tool.OTHER, 1)
        )
    }
}