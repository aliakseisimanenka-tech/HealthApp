package com.lsimanenka.shared.database

import com.lsimanenka.shared.datastore.TokenManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class SharedStatsProviderImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val tokenManager: TokenManager
) : SharedStatsProvider {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAggregatedStatsFlow(startTime: Long, endTime: Long, category: String?): Flow<AggregatedStats> {

        return tokenManager.userIdFlow.flatMapLatest { userId ->
            workoutDao.getAggregatedStatsFlow(
                startTime = startTime,
                endTime = endTime,
                category = category,
                ownerId = userId
            )
        }
    }
}
