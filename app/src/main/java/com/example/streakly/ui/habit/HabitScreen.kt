package com.example.streakly.ui.habit

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.streakly.data.local.entity.Habit
import com.example.streakly.ui.components.HabitItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    viewModel: HabitViewModel = viewModel()
) {
    val habits by viewModel.habits.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingHabit by remember { mutableStateOf<Habit?>(null) }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            "Streakly",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Track your progress, daily.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (habits.isEmpty()) {
                EmptyState(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(habits, key = { it.id }) { habit ->
                        HabitItem(
                            habit = habit,
                            onToggle = { viewModel.toggleHabit(habit) },
                            onDelete = { viewModel.deleteHabit(habit) },
                            onEdit = { editingHabit = habit }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            AddHabitDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, desc, goal, repeat ->
                    viewModel.addHabit(name, desc, goal = goal, repeat = repeat)
                    showAddDialog = false
                }
            )
        }

        if (editingHabit != null) {
            AddHabitDialog(
                habit = editingHabit,
                onDismiss = { editingHabit = null },
                onConfirm = { name, desc, goal, repeat ->
                    editingHabit?.let {
                        viewModel.updateHabit(it.copy(name = name, description = desc, goal = goal, repeat = repeat))
                    }
                    editingHabit = null
                }
            )
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "No habits yet!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Tap the + button to start your journey.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitDialog(
    habit: Habit? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(habit?.name ?: "") }
    var description by remember { mutableStateOf(habit?.description ?: "") }
    var goal by remember { mutableStateOf(habit?.goal ?: "") }
    var repeat by remember { mutableStateOf(habit?.repeat ?: "Daily") }

    val repeatOptions = listOf("Daily", "Weekly", "Monthly")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (habit == null) "New Habit" else "Edit Habit", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit Name") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = goal,
                    onValueChange = { goal = it },
                    label = { Text("Set Goal (e.g., 2 liters, 30 mins)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = repeat,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Repeat") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        repeatOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    repeat = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name, description, goal, repeat) },
                enabled = name.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (habit == null) "Add" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(28.dp)
    )
}
