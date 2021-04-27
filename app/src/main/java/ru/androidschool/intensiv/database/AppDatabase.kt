package ru.androidschool.intensiv.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LikedMovie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLikedMovieDao(): LikedMovieDao
}