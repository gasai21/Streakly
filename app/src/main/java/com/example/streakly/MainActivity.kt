package com.example.streakly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.streakly.ui.habit.AddEditHabitScreen
import com.example.streakly.ui.habit.HabitViewModel
import com.example.streakly.ui.habit.HomeScreen
import com.example.streakly.ui.onboarding.OnboardingScreen
import com.example.streakly.ui.theme.StreaklyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreaklyTheme {
                val viewModel: HabitViewModel = viewModel()
                val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (isOnboardingCompleted) "home" else "onboarding"
                ) {
                    composable("onboarding") {
                        OnboardingScreen(
                            onComplete = { name, habits ->
                                viewModel.completeOnboarding(name, habits)
                                navController.navigate("home") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onAddHabit = { navController.navigate("add_habit") },
                            onEditHabit = { habitId -> navController.navigate("edit_habit/$habitId") }
                        )
                    }
                    composable("add_habit") {
                        AddEditHabitScreen(
                            onBack = { navController.popBackStack() },
                            onSave = { habit ->
                                viewModel.addHabit(
                                    name = habit.name,
                                    description = habit.description,
                                    emoji = habit.iconEmoji,
                                    duration = habit.durationMinutes,
                                    goal = habit.goal,
                                    targetValue = habit.targetValue,
                                    unit = habit.unit,
                                    repeat = habit.repeat
                                )
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = "edit_habit/{habitId}",
                        arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val habitId = backStackEntry.arguments?.getInt("habitId")
                        val habits by viewModel.habits.collectAsState()
                        val habit = habits.find { it.id == habitId }
                        
                        AddEditHabitScreen(
                            habit = habit,
                            onBack = { navController.popBackStack() },
                            onSave = { updatedHabit ->
                                viewModel.updateHabit(updatedHabit)
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
