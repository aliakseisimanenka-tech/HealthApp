package com.lsimanenka.healthapp.features.home.domain

data class DailyMetrics(
    val cardioPoints: Int,
    val steps: Int,
    val calories: Int,
    val distanceKm: Double,
    val activeMinutes: Int
)