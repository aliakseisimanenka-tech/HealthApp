package com.lsimanenka.healthapp.features.workout.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsimanenka.healthapp.features.workout.domain.Workout
import com.lsimanenka.healthapp.features.workout.domain.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: WorkoutRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        handle.get<AddWorkoutContract.State>(KEY_STATE) ?: AddWorkoutContract.State()
    )
    val state: StateFlow<AddWorkoutContract.State> = _uiState.asStateFlow()

    private val _effect = Channel<AddWorkoutContract.SideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: AddWorkoutContract.Intent) {
        updateState { currentState ->
            AddWorkoutContract.Reducer.reduce(currentState, intent)
        }

        when (intent) {
            AddWorkoutContract.Intent.OnCloseClick -> {
                sendEffect(AddWorkoutContract.SideEffect.NavigateBack)
            }
            AddWorkoutContract.Intent.OnSaveClick -> {
                validateAndSaveWorkout()
            }
            else -> {}
        }
    }

    private fun validateAndSaveWorkout() {
        val currentState = state.value

        val timeParts = currentState.time.split(":")
        val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 0
        val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0

        val localDate = Instant.ofEpochMilli(currentState.date).atZone(ZoneOffset.UTC).toLocalDate()
        val localTime = LocalTime.of(hour, minute)

        val finalTimestamp = LocalDateTime.of(localDate, localTime)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val validatedState = state.value.validate()
        updateState { validatedState }

        if (validatedState.hasError) {
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
                distance = (currentState.distance.value.toIntOrNull()?.times(1000)) ?: 0,
                kcal = currentState.calories.value.toIntOrNull() ?: 0,
                steps = currentState.steps.value.toIntOrNull() ?: 0,
                notes = currentState.notes
            )

            repository.saveWorkout(workout)
            sendEffect(AddWorkoutContract.SideEffect.NavigateBack)
        }
    }

    private fun updateState(update: (AddWorkoutContract.State) -> AddWorkoutContract.State) {
        _uiState.update { current ->
            val updated = update(current)
            handle[KEY_STATE] = updated
            updated
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