package com.bettereveryday.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val categoryEmoji: String,
    val scheduleType: String,
    val reminderHour: Int,
    val reminderMinute: Int,
    val focusArea: String,
    val createdAt: Long,
    val sortOrder: Int,
)
