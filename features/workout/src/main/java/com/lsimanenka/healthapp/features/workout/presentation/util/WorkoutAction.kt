package com.lsimanenka.healthapp.features.workout.presentation.util

import androidx.annotation.StringRes
import com.lsimanenka.healthapp.features.workout.R

enum class WorkoutAction(@StringRes val titleResId: Int) {
    RUNNING(R.string.workout_action_running),
    SWIMMING(R.string.workout_action_swimming),
    CYCLING(R.string.workout_action_cycling),
    FOOTBALL(R.string.workout_action_football),
    BASKETBALL(R.string.workout_action_basketball),
    TENNIS(R.string.workout_action_tennis),
    CROSSFIT(R.string.workout_action_crossfit),
    ROWING(R.string.workout_action_rowing),
    BOXING(R.string.workout_action_boxing),
    CLIMBING(R.string.workout_action_climbing)
}