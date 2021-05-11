package com.flametech.vaytoday.utils.saved

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flametech.vaytoday.utils.saved.SavedDao

@Database(entities = [Saved::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSavedDao(): SavedDao
}