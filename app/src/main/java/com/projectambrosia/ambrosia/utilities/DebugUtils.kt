package com.projectambrosia.ambrosia.utilities

import android.content.Context
import com.projectambrosia.ambrosia.data.AmbrosiaDatabase
import com.projectambrosia.ambrosia.data.models.HSEntry
import com.projectambrosia.ambrosia.data.models.JournalEntry
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

const val DEBUG_USER_ID = "d9b3ceaa-e989-4cc0-ac3c-ddcae7b12b74\""

suspend fun prepopulateDatabase(context: Context) {
    withContext(Dispatchers.IO) {
        val db = AmbrosiaDatabase.getInstance(context)
        
        db.userDao.insert(
            User(DEBUG_USER_ID, "test@test.com", "Jane Doe", 1, "I want to live my life free from the control of food and love my body for the way it is.", Calendar.getInstance())
        )

        db.taskDao.insert(
            Task(1, DEBUG_USER_ID, Calendar.getInstance(), "Today - Journal", 1, Tool.JOURNAL, 1),
            Task(2, DEBUG_USER_ID, Calendar.getInstance(), "Today - HS", 1, Tool.HS, 1),
            Task(3, DEBUG_USER_ID, Calendar.getInstance(), "Today - IEAS", 1, Tool.IEAS, 1),
            Task(4, DEBUG_USER_ID, Calendar.getInstance(), "Today - Other", 1, Tool.OTHER, 1),
            Task(5, DEBUG_USER_ID, Calendar.getInstance(), "Today - Other - Completed", 1, Tool.OTHER, 1, true),

            Task(6, DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000) }, "Yesterday - Other - Completed", 1, Tool.OTHER, 1, true),
            Task(7, DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*2) }, "2 days ago - Other - Completed", 1, Tool.OTHER, 1, true),
            Task(8, DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*3) }, "3 days ago - Other - Completed", 1, Tool.OTHER, 1, true),
            Task(9, DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*4) }, "4 days ago - Other - Completed", 1, Tool.OTHER, 1, true)
        )

        db.journalEntryDao.insert(
            JournalEntry(DEBUG_USER_ID, Calendar.getInstance(), "Prompt 1", "Entry 1", null),
            JournalEntry(DEBUG_USER_ID, Calendar.getInstance(), "Prompt 2", "Entry 2", null),
            JournalEntry(DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000) }, "Prompt 3", "Entry 3", null),
            JournalEntry(DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000) }, "Prompt 4", "Entry 4", null),
            JournalEntry(DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*2) }, "Prompt 5", "Entry 5", null),
            JournalEntry(DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*3) }, "Prompt 6", "Entry 6", null)
        )

        db.hsEntryDao.insert(
            HSEntry(DEBUG_USER_ID, Calendar.getInstance(), 1, null, "hs entry 1"),
            HSEntry(DEBUG_USER_ID, Calendar.getInstance(), 2, 9, "hs entry 2"),
            HSEntry(DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000) }, 3, 7, "hs entry 3"),
            HSEntry(DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000) }, 2, 6, "hs entry 4"),
            HSEntry(DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*2) }, 5, 8, "hs entry 5"),
            HSEntry(DEBUG_USER_ID, Calendar.getInstance().apply { timeInMillis = timeInMillis.minus(86400000*3) }, 4, 7, "hs entry 6")
        )
    }
}