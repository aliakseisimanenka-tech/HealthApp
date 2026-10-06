package com.lsimanenka.shared.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface WorkoutDao {

    @Upsert
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Query("SELECT * FROM workouts_table WHERE date >= :startOfDay AND date <= :endOfDay")
    suspend fun getWorkoutsForDay(startOfDay: Long, endOfDay: Long): List<WorkoutEntity>

    @Query("""
        SELECT 
            IFNULL(SUM(cardio), 0) as totalCardio,
            IFNULL(SUM(steps), 0) as totalSteps,
            IFNULL(SUM(kcal), 0) as totalCalories,
            IFNULL(SUM(distance), 0) as totalDistance,
            IFNULL(SUM(duration), 0) as totalDuration
        FROM workouts_table 
        WHERE date BETWEEN :startTime AND :endTime
        AND (:category IS NULL OR action = :category)
    """)
    suspend fun getAggregatedStats(
        startTime: Long,
        endTime: Long,
        category: String? = null
    ): AggregatedStats
}