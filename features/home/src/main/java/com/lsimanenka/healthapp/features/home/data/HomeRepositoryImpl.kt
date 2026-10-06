package com.lsimanenka.healthapp.features.home.data

import com.lsimanenka.healthapp.features.home.domain.DailyMetrics
import com.lsimanenka.healthapp.features.home.domain.HomeRepository
import com.lsimanenka.shared.database.SharedStatsProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val sharedStatsProvider: SharedStatsProvider
) : HomeRepository {

    override fun getDailyMetrics(): Flow<DailyMetrics> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val startOfDay = today.atStartOfDay(zone).toInstant().toEpochMilli()
        val endOfDay = today.atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()

        return sharedStatsProvider.getStats(startOfDay, endOfDay)
            .map { stats ->
                DailyMetrics(
                    cardioPoints = stats.totalCardio,
                    steps = stats.totalSteps,
                    calories = stats.totalCalories,
                    distanceKm = stats.totalDistance / 1000.0,
                    activeMinutes = stats.totalDuration
                )
            }
    }
}