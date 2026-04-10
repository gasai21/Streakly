package com.example.streakly.ui.habit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.streakly.data.local.database.HabitDatabase
import com.example.streakly.data.local.entity.Habit
import com.example.streakly.data.local.entity.User
import com.example.streakly.data.repository.HabitRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository
    private val userDao = HabitDatabase.getDatabase(application).userDao()
    
    val habits: StateFlow<List<Habit>>
    
    private val _user = userDao.getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val userName: StateFlow<String> = _user.map { it?.name ?: "" }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val isOnboardingCompleted: StateFlow<Boolean> = _user.map { it?.isOnboardingCompleted ?: false }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    init {
        val habitDao = HabitDatabase.getDatabase(application).habitDao()
        repository = HabitRepository(habitDao)
        habits = repository.allHabits.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun completeOnboarding(name: String, initialHabits: List<Habit>) {
        viewModelScope.launch {
            userDao.insertUser(User(name = name, isOnboardingCompleted = true))
            initialHabits.forEach { repository.insert(it) }
        }
    }

    fun addHabit(name: String, description: String = "", emoji: String = "✨", duration: Int = 5) {
        viewModelScope.launch {
            repository.insert(Habit(name = name, description = description, iconEmoji = emoji, durationMinutes = duration))
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            repository.update(habit)
        }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch {
            repository.toggleHabit(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.delete(habit)
        }
    }
}
