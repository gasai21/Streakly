package com.example.streakly.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import com.example.streakly.ui.habit.AddEditHabitScreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streakly.data.local.entity.Habit

@Composable
fun OnboardingScreen(
    onComplete: (String, List<Habit>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var step by remember { mutableIntStateOf(1) }
    val selectedHabits = remember { mutableStateListOf<Habit>() }
    var habitToEdit by remember { mutableStateOf<Habit?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val suggestedHabits = remember {
        mutableStateListOf(
            Habit(name = "Drink water", goal = "2 Liters", durationMinutes = 5, iconEmoji = "🥤"),
            Habit(name = "Meditate", goal = "15 Mins", durationMinutes = 15, iconEmoji = "🧘"),
            Habit(name = "Read a book", goal = "10 Pages", durationMinutes = 20, iconEmoji = "📚"),
            Habit(name = "Morning exercise", goal = "30 Mins", durationMinutes = 30, iconEmoji = "🏃"),
            Habit(name = "Journaling", goal = "1 Page", durationMinutes = 10, iconEmoji = "✍️")
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF7F2F0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            if (step == 1) {
                Text(
                    text = "Welcome to Streakly!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E342E) // Match brownish theme color
                )
                Text(
                    text = "Let's start by getting to know you.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("What's your name?") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4E342E),
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = Color(0xFF4E342E),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = { if (name.isNotBlank()) step = 2 },
                    enabled = name.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E342E))
                ) {
                    Text("Next", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
            } else {
                Text(
                    text = "Hi, $name!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E342E)
                )
                Text(
                    text = "What habits do you want to track?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFF4E342E)),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF4E342E))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Custom Habit", color = Color(0xFF4E342E))
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(suggestedHabits) { habit ->
                        val isSelected = selectedHabits.any { it.name == habit.name }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isSelected) {
                                        selectedHabits.removeIf { it.name == habit.name }
                                    } else {
                                        selectedHabits.add(habit)
                                    }
                                },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFFFFD1B2) else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(if (isSelected) Color.White else Color(0xFFFBE9E7), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(habit.iconEmoji, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = habit.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (habit.goal.isNotBlank()) {
                                        Text(
                                            text = habit.goal,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isSelected) Color(0xFFFF6D00) else Color.Gray
                                        )
                                    }
                                }
                                if (isSelected) {
                                    IconButton(onClick = { habitToEdit = habit }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFFFF6D00))
                                    }
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFFF6D00))
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { 
                        val numberRegex = """(\d+\.?\d*)""".toRegex()
                        val finalHabits = selectedHabits.map { habit ->
                            val match = numberRegex.find(habit.goal)
                            val numericGoal = match?.value?.toDoubleOrNull() ?: 1.0
                            val unit = if (match != null) habit.goal.replace(match.value, "").trim() else ""
                            
                            habit.copy(
                                targetValue = numericGoal,
                                unit = unit
                            )
                        }
                        onComplete(name, finalHabits) 
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E342E))
                ) {
                    Text("Let's Go!", style = MaterialTheme.typography.titleMedium, color = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showAddDialog) {
            AddEditHabitScreen(
                onBack = { showAddDialog = false },
                onSave = { habit ->
                    suggestedHabits.add(0, habit)
                    selectedHabits.add(habit)
                    showAddDialog = false
                }
            )
        }

        if (habitToEdit != null) {
            AddEditHabitScreen(
                habit = habitToEdit,
                onBack = { habitToEdit = null },
                onSave = { updatedHabit ->
                    // Update in suggested list
                    val indexInSuggested = suggestedHabits.indexOfFirst { it.name == habitToEdit!!.name }
                    if (indexInSuggested != -1) {
                        suggestedHabits[indexInSuggested] = updatedHabit
                    }
                    
                    // Update in selected list
                    val indexInSelected = selectedHabits.indexOfFirst { it.name == habitToEdit!!.name }
                    if (indexInSelected != -1) {
                        selectedHabits[indexInSelected] = updatedHabit
                    }
                    
                    habitToEdit = null
                }
            )
        }
    }
}
