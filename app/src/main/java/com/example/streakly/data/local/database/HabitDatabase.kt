package com.example.streakly.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.streakly.data.local.dao.HabitDao
import com.example.streakly.data.local.dao.UserDao
import com.example.streakly.data.local.entity.Habit
import com.example.streakly.data.local.entity.User

@Database(entities = [Habit::class, User::class], version = 3, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habit_database"
                )
                .fallbackToDestructiveMigration() // Simple for development
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
