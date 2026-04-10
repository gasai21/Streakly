package com.example.streakly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
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

                if (isOnboardingCompleted) {
                    HomeScreen(viewModel)
                } else {
                    OnboardingScreen(
                        onComplete = { name, habits ->
                            viewModel.completeOnboarding(name, habits)
                        }
                    )
                }
            }
        }
    }
}
