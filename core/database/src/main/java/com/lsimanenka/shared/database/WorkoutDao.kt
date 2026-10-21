package com.lsimanenka.shared.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

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
        AND isDeleted = 0 
        AND ownerId IS :ownerId
    """)
    fun getAggregatedStatsFlow(
        startTime: Long,
        endTime: Long,
        category: String? = null,
        ownerId: String? = null
    ): Flow<AggregatedStats>

    @Query("SELECT * FROM workouts_table WHERE isSynced = 0 AND isDeleted = 0 AND ownerId IS NOT NULL")
    suspend fun getUnsyncedWorkouts(): List<WorkoutEntity>

    @Query("UPDATE workouts_table SET ownerId = :newOwnerId, isSynced = 0 WHERE ownerId IS NULL")
    suspend fun assignAnonymousWorkoutsToUser(newOwnerId: String)

    @Query("DELETE FROM workouts_table WHERE ownerId IS NOT NULL")
    suspend fun clearAllUserWorkouts()

    @Query("DELETE FROM workouts_table WHERE ownerId = :ownerId AND isSynced = 1")
    suspend fun clearSyncedUserWorkouts(ownerId: String)
}