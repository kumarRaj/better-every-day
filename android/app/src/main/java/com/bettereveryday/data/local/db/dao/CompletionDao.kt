package com.bettereveryday.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bettereveryday.data.local.db.entity.CompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletionDao {

    @Query("SELECT * FROM completions WHERE completedDate = :date")
    fun getCompletionsForDate(date: String): Flow<List<CompletionEntity>>

    @Query("SELECT * FROM completions WHERE habitId = :habitId")
    fun getCompletionsForHabit(habitId: Long): Flow<List<CompletionEntity>>

    @Query("SELECT * FROM completions WHERE completedDate >= :start AND completedDate <= :end")
    fun getCompletionsInRange(start: String, end: String): Flow<List<CompletionEntity>>

    @Insert
    suspend fun insertCompletion(completion: CompletionEntity): Long

    @Query("DELETE FROM completions WHERE habitId = :habitId AND completedDate = :date")
    suspend fun deleteCompletion(habitId: Long, date: String)

    @Query("SELECT EXISTS(SELECT 1 FROM completions WHERE habitId = :habitId AND completedDate = :date)")
    suspend fun isCompleted(habitId: Long, date: String): Boolean
}
