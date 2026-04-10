package com.example.streakly.data.repository

import com.example.streakly.data.local.dao.HabitDao
import com.example.streakly.data.local.entity.Habit
import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()

    suspend fun insert(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    suspend fun update(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun delete(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun toggleHabit(habit: Habit) {
        val isCompleted = !habit.isCompletedToday
        val newStreak = if (isCompleted) habit.streak + 1 else maxOf(0, habit.streak - 1)
        habitDao.updateHabitStatus(habit.id, isCompleted, newStreak, System.currentTimeMillis())
    }
}
