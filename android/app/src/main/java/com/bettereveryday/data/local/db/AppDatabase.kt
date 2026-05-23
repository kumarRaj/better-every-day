package com.bettereveryday.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bettereveryday.data.local.db.dao.CompletionDao
import com.bettereveryday.data.local.db.dao.HabitDao
import com.bettereveryday.data.local.db.entity.CompletionEntity
import com.bettereveryday.data.local.db.entity.HabitEntity

@Database(
    entities = [HabitEntity::class, CompletionEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun completionDao(): CompletionDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "better_everyday.db",
                ).build().also { instance = it }
            }
    }
}
