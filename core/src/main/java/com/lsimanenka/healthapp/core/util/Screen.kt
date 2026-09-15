package com.lsimanenka.healthapp.core.util

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen
    @Serializable
    data object History : Screen
    @Serializable
    data object Profile : Screen
}