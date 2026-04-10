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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    val suggestedHabits = listOf(
        Habit(name = "Drink water", durationMinutes = 5, iconEmoji = "🥤"),
        Habit(name = "Meditate", durationMinutes = 15, iconEmoji = "🧘"),
        Habit(name = "Read a book", durationMinutes = 20, iconEmoji = "📚"),
        Habit(name = "Morning exercise", durationMinutes = 30, iconEmoji = "🏃"),
        Habit(name = "Journaling", durationMinutes = 10, iconEmoji = "✍️")
    )

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
                "Welcome to Streakly!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Let's start by getting to know you.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("What's your name?") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { if (name.isNotBlank()) step = 2 },
                enabled = name.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Next", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            Text(
                "Hi, $name!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "What habits do you want to track?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
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
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                                             else MaterialTheme.colorScheme.surface
                        ),
                        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(habit.iconEmoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(habit.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                            if (isSelected) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onComplete(name, selectedHabits.toList()) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Let's Go!", style = MaterialTheme.typography.titleMedium)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}
