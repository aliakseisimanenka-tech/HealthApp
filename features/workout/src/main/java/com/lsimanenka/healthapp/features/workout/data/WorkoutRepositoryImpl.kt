package com.lsimanenka.healthapp.features.workout.data

import com.lsimanenka.auth.domain.AuthRepository
import com.lsimanenka.healthapp.features.workout.domain.Workout
import com.lsimanenka.healthapp.features.workout.domain.WorkoutRepository
import com.lsimanenka.shared.database.WorkoutDao
import com.lsimanenka.shared.database.WorkoutEntity
import com.lsimanenka.shared.remote.WorkoutSyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val authRepository: AuthRepository,
    private val syncManager: WorkoutSyncManager
) : WorkoutRepository {

    override suspend fun saveWorkout(workout: Workout) {
        val currentUserId = authRepository.currentUserId.first()

        val entity = WorkoutEntity(
            id = workout.id,
            name = workout.name,
            action = workout.action,
            date = workout.date,
            duration = workout.duration,
            cardio = workout.cardio,
            distance = workout.distance,
            kcal = workout.kcal,
            steps = workout.steps,
            notes = workout.notes,
            ownerId = currentUserId,
            isSynced = false
        )

        workoutDao.insertWorkout(entity)
        CoroutineScope(Dispatchers.IO).launch {
            syncManager.sync()
        }
    }
}