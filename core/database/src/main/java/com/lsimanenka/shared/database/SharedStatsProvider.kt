package com.lsimanenka.shared.database

import kotlinx.coroutines.flow.Flow

interface SharedStatsProvider {
    fun getAggregatedStatsFlow(startTime: Long, endTime: Long, category: String? = null): Flow<AggregatedStats>
}