package com.lsimanenka.healthapp.features.workout.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsimanenka.healthapp.features.workout.domain.Workout
import com.lsimanenka.healthapp.features.workout.domain.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: WorkoutRepository
) : ViewModel() {

    val state = handle.getStateFlow(KEY_STATE, AddWorkoutContract.State())

    private val _effect = Channel<AddWorkoutContract.SideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: AddWorkoutContract.Intent) {
        val newState = AddWorkoutContract.Reducer.reduce(state.value, intent)
        handle[KEY_STATE] = newState

        when (intent) {
            AddWorkoutContract.Intent.OnCloseClick -> {
                sendEffect(AddWorkoutContract.SideEffect.NavigateBack)
            }
            AddWorkoutContract.Intent.OnSaveClick -> { saveWorkout() }
            else -> {}
        }
    }

    private fun saveWorkout() {
        viewModelScope.launch {
            val currentState = state.value

            Log.d("WORKOUT", "${currentState.name}, ${currentState.cardioPoints}, ${currentState.steps}")

            val workout = Workout(
                name = currentState.name.ifEmpty { "Тренировка" },
                action = currentState.actionType,
                date = System.currentTimeMillis(),
                duration = currentState.durationMin.toIntOrNull() ?: 30,
                cardio = currentState.cardioPoints.toIntOrNull() ?: 10,
                distance = currentState.distance.toIntOrNull() ?: 10,
                kcal = currentState.calories.toIntOrNull() ?: 10,
                steps = currentState.steps.toIntOrNull() ?: 10,
                notes = currentState.notes
            )

            repository.saveWorkout(workout)
            sendEffect(AddWorkoutContract.SideEffect.NavigateBack)
        }
    }

    private fun sendEffect(effect: AddWorkoutContract.SideEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    companion object {
        private const val KEY_STATE = "add_workout_state"
    }
}