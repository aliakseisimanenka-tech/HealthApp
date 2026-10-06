package com.lsimanenka.healthapp.features.home.domain

import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getDailyMetrics(): Flow<DailyMetrics>
}