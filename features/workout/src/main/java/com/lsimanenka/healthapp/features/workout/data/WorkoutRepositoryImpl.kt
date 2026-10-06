package com.lsimanenka.healthapp.features.workout.data

import com.lsimanenka.healthapp.features.workout.domain.Workout
import com.lsimanenka.healthapp.features.workout.domain.WorkoutRepository
import com.lsimanenka.shared.database.WorkoutDao
import com.lsimanenka.shared.database.WorkoutEntity
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {

    override suspend fun saveWorkout(workout: Workout) {
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
            notes = workout.notes
        )
        workoutDao.insertWorkout(entity)
    }
}