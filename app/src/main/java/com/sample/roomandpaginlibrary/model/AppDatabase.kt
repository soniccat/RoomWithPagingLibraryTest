package com.sample.roomandpaginlibrary.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Word::class, Translation::class],
        version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): DBDao
}