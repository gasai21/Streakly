package com.example.streakly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val streak: Int = 0,
    val isCompletedToday: Boolean = false,
    val lastCompletedTimestamp: Long = 0L,
    val durationMinutes: Int = 5,
    val iconEmoji: String = "✨",
    val repeat: String = "Daily",
    val goal: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
