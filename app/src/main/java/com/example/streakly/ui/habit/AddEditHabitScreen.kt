package com.example.streakly.ui.habit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streakly.data.local.entity.Habit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    habit: Habit? = null,
    onBack: () -> Unit,
    onSave: (Habit) -> Unit
) {
    var name by remember { mutableStateOf(habit?.name ?: "") }
    var description by remember { mutableStateOf(habit?.description ?: "") }
    var goal by remember { mutableStateOf(habit?.goal ?: "") }
    var iconEmoji by remember { mutableStateOf(habit?.iconEmoji ?: "✨") }
    
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val selectedDays = remember { 
        mutableStateListOf<String>().apply {
            if (habit != null && habit.repeat.contains(",")) {
                addAll(habit.repeat.split(","))
            } else if (habit?.repeat == "Daily") {
                addAll(daysOfWeek)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (habit == null) "Create Habit" else "Edit Habit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF7F2F0)
                )
            )
        },
        containerColor = Color(0xFFF7F2F0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Icon Selector
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFBE9E7)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(iconEmoji, fontSize = 40.sp)
                }

                val emojis = listOf("✨", "🥤", "🧘", "📚", "🏃", "✍️", "🍎", "💪", "💧", "🥦", "🚶", "🚴", "🏊", "🛌", "🌱", "☀️", "🌙", "🎸", "🎨", "🧩")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(emojis) { emoji ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (iconEmoji == emoji) Color(0xFF4E342E) else Color.White)
                                .clickable { iconEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 24.sp)
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = buildAnnotatedString {
                        append("Habit Name")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E342E)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("e.g., Drink Water") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4E342E),
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = buildAnnotatedString {
                        append("Set Goal")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E342E)
                )
                OutlinedTextField(
                    value = goal,
                    onValueChange = { goal = it },
                    placeholder = { Text("e.g., 2 Liters") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4E342E),
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = buildAnnotatedString {
                        append("Repeat")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E342E)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysOfWeek.forEach { day ->
                        val isSelected = selectedDays.contains(day)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color(0xFF4E342E) else Color.White)
                                .clickable {
                                    if (isSelected) selectedDays.remove(day) else selectedDays.add(day)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.first().toString(),
                                color = if (isSelected) Color.White else Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                if (selectedDays.isNotEmpty() && goal.isNotBlank()) {
                    val numberRegex = """(\d+\.?\d*)""".toRegex()
                    val match = numberRegex.find(goal)
                    val numericGoal = match?.value?.toDoubleOrNull()

                    if (numericGoal != null) {
                        val unit = goal.replace(match.value, "").trim()
                        val total = numericGoal * selectedDays.size
                        val totalFormatted = if (total % 1.0 == 0.0) total.toInt().toString() else "%.1f".format(total)
                        
                        Surface(
                            color = Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, Color(0xFFFFB74D).copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("💡", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Weekly target: $totalFormatted $unit",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF4E342E),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Based on $numericGoal $unit x ${selectedDays.size} days",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF4E342E).copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Description", fontWeight = FontWeight.Bold, color = Color(0xFF4E342E))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Optional") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4E342E),
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val repeatStr = if (selectedDays.size == 7) "Daily" else selectedDays.joinToString(",")
                    val numberRegex = """(\d+\.?\d*)""".toRegex()
                val match = numberRegex.find(goal)
                val numericGoal = match?.value?.toDoubleOrNull() ?: 0.0
                val unit = if (match != null) goal.replace(match.value, "").trim() else ""

                val finalHabit = (habit ?: Habit(name = name)).copy(
                    name = name,
                    description = description,
                    goal = goal,
                    targetValue = numericGoal,
                    unit = unit,
                    repeat = repeatStr,
                    iconEmoji = iconEmoji
                )
                onSave(finalHabit)
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E342E)),
                enabled = name.isNotBlank() && selectedDays.isNotEmpty()
            ) {
                Text("Save Habit", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
