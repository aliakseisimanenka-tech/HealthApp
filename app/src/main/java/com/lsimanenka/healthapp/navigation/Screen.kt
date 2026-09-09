package com.lsimanenka.healthapp.navigation

import kotlinx.serialization.Serializable

@Serializable sealed interface Screen {
    @Serializable data object Home : Screen
    @Serializable data object History : Screen
    @Serializable data object Profile : Screen
}