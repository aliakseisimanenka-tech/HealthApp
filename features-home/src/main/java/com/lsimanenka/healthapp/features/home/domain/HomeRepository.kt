package com.lsimanenka.healthapp.features.home.domain

interface HomeRepository {
    suspend fun getDailyMetrics(): DailyMetrics
}