package com.example.streakly.ui.habit

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.streakly.data.local.entity.Habit
import com.example.streakly.ui.components.HabitItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HabitViewModel = viewModel()
) {
    val habits by viewModel.habits.collectAsState()
    val userName by viewModel.userName.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var habitToEdit by remember { mutableStateOf<Habit?>(null) }

    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF4E342E), 
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        },
        containerColor = Color(0xFFF7F2F0) 
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Morning, $userName",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        today.format(formatter),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE0B2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🦁", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Dynamic Calendar Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dateList = remember {
                    ( -3..3).map { today.plusDays(it.toLong()) }
                }

                dateList.forEach { date ->
                    val isSelected = date == today
                    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Color(0xFF263238) else Color.Transparent)
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = dayName, 
                            color = if (isSelected) Color.White else Color.Gray, 
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = date.dayOfMonth.toString(),
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Reminder Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD1B2)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Set the reminder",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4E342E)
                        )
                        Text(
                            text = "Never miss your morning routine!\nSet a reminder to stay on track.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black.copy(alpha = 0.6f),
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E342E)),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text(
                                text = "Set Now",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                    }
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color(0xFFFF6D00)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Habits List Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Daily routine", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(onClick = { }) {
                    Text("See all", color = Color.Gray)
                }
            }

            // Habits List
            habits.forEach { habit ->
                HabitItem(
                    habit = habit,
                    onToggle = { viewModel.toggleHabit(habit) },
                    onDelete = { viewModel.deleteHabit(habit) },
                    onEdit = { habitToEdit = habit }
                )
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }

        if (showAddDialog) {
            AddHabitDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, desc ->
                    viewModel.addHabit(name, desc)
                    showAddDialog = false
                }
            )
        }

        if (habitToEdit != null) {
            AddHabitDialog(
                habit = habitToEdit,
                onDismiss = { habitToEdit = null },
                onConfirm = { name, desc ->
                    // Since I don't have updateHabit yet, I'll delete and re-add for simplicity or better, add updateHabit to ViewModel
                    viewModel.deleteHabit(habitToEdit!!)
                    viewModel.addHabit(name, desc)
                    habitToEdit = null
                }
            )
        }
    }
}
