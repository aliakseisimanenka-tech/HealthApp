package com.lsimanenka.healthapp.features.workout.presentation

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
            AddWorkoutContract.Intent.OnSaveClick -> {
                saveWorkout()
            }
            else -> {}
        }
    }

    private fun saveWorkout() {
        val currentState = state.value

        val timeParts = currentState.time.split(":")
        val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 0
        val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
        val finalTimestamp = currentState.date + (hour * 3600_000L) + (minute * 60_000L)

        val isNameValid = currentState.name.value.isNotBlank()

        val isCardioValid = currentState.cardioPoints.value.isBlank() ||
                (currentState.cardioPoints.value.toIntOrNull() != null && currentState.cardioPoints.value.toInt() >= 0)

        val isDistanceValid = currentState.distance.value.isBlank() ||
                (currentState.distance.value.toIntOrNull() != null && currentState.distance.value.toInt() >= 0)

        val isCaloriesValid = currentState.calories.value.isBlank() ||
                (currentState.calories.value.toIntOrNull() != null && currentState.calories.value.toInt() >= 0)

        val isStepsValid = currentState.steps.value.isBlank() ||
                (currentState.steps.value.toIntOrNull() != null && currentState.steps.value.toInt() >= 0)

        val hasError = !isNameValid || !isCardioValid || !isDistanceValid || !isCaloriesValid || !isStepsValid

        if (hasError) {
            handle[KEY_STATE] = currentState.copy(
                name = currentState.name.copy(isError = !isNameValid),
                cardioPoints = currentState.cardioPoints.copy(isError = !isCardioValid),
                distance = currentState.distance.copy(isError = !isDistanceValid),
                calories = currentState.calories.copy(isError = !isCaloriesValid),
                steps = currentState.steps.copy(isError = !isStepsValid)
            )

            sendEffect(AddWorkoutContract.SideEffect.ShowSnackbar("Пожалуйста, корректно заполните выделенные поля"))
            return
        }

        viewModelScope.launch {
            val workout = Workout(
                name = currentState.name.value,
                action = currentState.actionType,
                date = finalTimestamp,
                duration = currentState.duration,
                cardio = currentState.cardioPoints.value.toIntOrNull() ?: 0,
                distance = currentState.distance.value.toIntOrNull() ?: 0,
                kcal = currentState.calories.value.toIntOrNull() ?: 0,
                steps = currentState.steps.value.toIntOrNull() ?: 0,
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