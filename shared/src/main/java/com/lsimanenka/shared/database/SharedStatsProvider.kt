package com.lsimanenka.shared.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SharedStatsProvider @Inject constructor(
    private val workoutDao: WorkoutDao
) {

    fun getStats(startTime: Long, endTime: Long, category: String? = null): Flow<AggregatedStats> {
        return workoutDao.getAggregatedStats(startTime, endTime, category)
    }
}