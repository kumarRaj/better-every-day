package com.bettereveryday.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bettereveryday.data.local.db.dao.CompletionDao
import com.bettereveryday.data.local.db.dao.HabitDao
import com.bettereveryday.data.local.db.entity.CompletionEntity
import com.bettereveryday.data.local.db.entity.HabitEntity

@Database(
    entities = [HabitEntity::class, CompletionEntity::class],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun completionDao(): CompletionDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS feedback (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, subject TEXT NOT NULL, body TEXT NOT NULL, submittedAt INTEGER NOT NULL)"
                )
            }
        }

        // The feedback table was created in 1→2 but never added as a Room entity,
        // causing a hash mismatch. 2→3 drops that orphan table to realign the schema.
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS feedback")
            }
        }

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "better_everyday.db",
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build().also { instance = it }
            }
    }
}
