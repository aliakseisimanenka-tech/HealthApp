package com.lsimanenka.shared.database

import javax.inject.Inject

class SharedStatsProvider @Inject constructor(
    private val workoutDao: WorkoutDao
) {

    suspend fun getStats(startTime: Long, endTime: Long, category: String? = null): AggregatedStats {
        return workoutDao.getAggregatedStats(startTime, endTime, category)
    }
}