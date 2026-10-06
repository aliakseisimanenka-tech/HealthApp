package com.lsimanenka.healthapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lsimanenka.healthapp.core.util.Screen
import com.lsimanenka.healthapp.features.history.HistoryScreen
import com.lsimanenka.healthapp.features.home.presentation.HomeScreen
import com.lsimanenka.healthapp.features.profile.ProfileScreen
import com.lsimanenka.healthapp.features.workout.presentation.WorkoutScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable<Screen.Home> { HomeScreen(
            onNavigateToAddWorkout = {
                navController.navigate(Screen.Workout)
            }
        ) }
        composable<Screen.History> { HistoryScreen(
            onNavigateToAddWorkout = {
                navController.navigate(Screen.Workout)
            }
        ) }
        composable<Screen.Profile> { ProfileScreen() }
        composable<Screen.Workout> {
            WorkoutScreen(
                onClose = { navController.popBackStack() }
            )
        }
    }
}