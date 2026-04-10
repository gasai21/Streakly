package com.example.streakly.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class User(
    @PrimaryKey val id: Int = 1, // We only ever have one user
    val name: String,
    val isOnboardingCompleted: Boolean = false
)
