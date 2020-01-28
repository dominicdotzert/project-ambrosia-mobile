package com.projectambrosia.ambrosia.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.projectambrosia.ambrosia.utilities.DATABASE_NAME
import com.projectambrosia.ambrosia.utilities.prepopulateDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Task::class,
        JournalEntry::class,
        HSEntry::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AmbrosiaDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val taskDao: TaskDao
    abstract val journalEntryDao: JournalEntryDao
    abstract val hsEntryDao: HSEntryDao

    companion object {
        @Volatile
        private var INSTANCE: AmbrosiaDatabase? = null

        // TODO: Remove pre-populate step when able to test with server
        fun getInstance(context: Context): AmbrosiaDatabase {
            return INSTANCE ?: synchronized(this) {
                return Room.databaseBuilder(
                    context.applicationContext,
                    AmbrosiaDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            MainScope().launch {
                                prepopulateDatabase(context)
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}