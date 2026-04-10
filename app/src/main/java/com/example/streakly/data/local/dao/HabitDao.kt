package com.example.streakly.data.local.dao

import androidx.room.*
import com.example.streakly.data.local.entity.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("UPDATE habits SET isCompletedToday = :isCompleted, streak = :streak, lastCompletedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateHabitStatus(id: Int, isCompleted: Boolean, streak: Int, timestamp: Long)
}
