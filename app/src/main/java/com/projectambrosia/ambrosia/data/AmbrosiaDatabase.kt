package com.projectambrosia.ambrosia.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.projectambrosia.ambrosia.utilities.DATABASE_NAME

@Database(
    entities = [
        User::class,
        Task::class,
        JournalEntry::class,
        HSEntry::class
    ],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class AmbrosiaDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val taskDao: TaskDao
    abstract val journalEntryDao: JournalEntryDao
    abstract val hsEntryDao: HSEntryDao

    companion object {
        @Volatile private var INSTANCE: AmbrosiaDatabase? = null

        fun getInstance(context: Context): AmbrosiaDatabase {
            return INSTANCE?: synchronized(this) {
                return Room.databaseBuilder(
                    context.applicationContext,
                    AmbrosiaDatabase::class.java,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}