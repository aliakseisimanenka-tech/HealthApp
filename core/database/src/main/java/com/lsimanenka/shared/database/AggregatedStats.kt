package com.lsimanenka.shared.database

data class AggregatedStats(
    val totalCardio: Int,
    val totalSteps: Int,
    val totalCalories: Int,
    val totalDistance: Int,
    val totalDuration: Int
)