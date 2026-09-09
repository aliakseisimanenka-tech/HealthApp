package com.lsimanenka.healthapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lsimanenka.healthapp.features.history.HistoryScreen
import com.lsimanenka.healthapp.features.home.HomeScreen
import com.lsimanenka.healthapp.features.profile.ProfileScreen

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
        composable<Screen.Home> { HomeScreen() }
        composable<Screen.History> { HistoryScreen() }
        composable<Screen.Profile> { ProfileScreen() }
    }
}