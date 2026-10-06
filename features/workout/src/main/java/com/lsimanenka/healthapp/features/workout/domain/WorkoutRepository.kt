package com.lsimanenka.healthapp.features.workout.domain

interface WorkoutRepository {
    suspend fun saveWorkout(workout: Workout)
}