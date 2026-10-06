package com.lsimanenka.healthapp.features.workout.domain

data class Workout(
    val id: Int = 0,
    val name: String,
    val action: String,
    val date: Long,
    val duration: Int,
    val cardio: Int,
    val distance: Int,
    val kcal: Int,
    val steps: Int,
    val notes: String
)