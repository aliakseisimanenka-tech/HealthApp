package com.lsimanenka.healthapp.features.workout.presentation

import android.os.Parcelable
import com.lsimanenka.healthapp.features.workout.presentation.util.FieldState
import kotlinx.parcelize.Parcelize

interface AddWorkoutContract {

    @Parcelize
    data class State(
        val name: FieldState = FieldState("Без названия"),
        val actionType: String = "Ходьба",
        val date: Long = System.currentTimeMillis(),
        val time: String = "12:00",
        val duration: Int = 30,
        val cardioPoints: FieldState = FieldState(""),
        val distance: FieldState = FieldState(""),
        val calories: FieldState = FieldState(""),
        val steps: FieldState = FieldState(""),
        val notes: String = ""
    ) : Parcelable

    sealed class Intent {
        object OnCloseClick : Intent()
        object OnSaveClick : Intent()

        data class UpdateName(val name: String) : Intent()
        data class UpdateActionType(val type: String) : Intent()
        data class UpdateDate(val dateMillis: Long) : Intent()
        data class UpdateTime(val time: String) : Intent()
        data class UpdateDuration(val minutes: Int) : Intent()
        data class UpdateCardio(val cardio: String) : Intent()
        data class UpdateDistance(val distance: String) : Intent()
        data class UpdateCalories(val calories: String) : Intent()
        data class UpdateSteps(val steps: String) : Intent()
        data class UpdateNotes(val notes: String) : Intent()
    }

    sealed interface SideEffect {
        object NavigateBack : SideEffect
        data class ShowSnackbar(val message: String) : SideEffect
    }

    object Reducer {
        fun reduce(state: State, intent: Intent): State {
            return when (intent) {
                Intent.OnCloseClick -> state
                Intent.OnSaveClick -> state

                is Intent.UpdateName -> state.copy(name = FieldState(intent.name, false))
                is Intent.UpdateDuration -> state.copy(duration = intent.minutes)
                is Intent.UpdateCardio -> state.copy(cardioPoints = FieldState(intent.cardio, false))
                is Intent.UpdateDistance -> state.copy(distance = FieldState(intent.distance, false))
                is Intent.UpdateCalories -> state.copy(calories = FieldState(intent.calories, false))
                is Intent.UpdateSteps -> state.copy(steps = FieldState(intent.steps, false))

                is Intent.UpdateActionType -> state.copy(actionType = intent.type)
                is Intent.UpdateTime -> state.copy(time = intent.time)
                is Intent.UpdateNotes -> state.copy(notes = intent.notes)
                is Intent.UpdateDate -> state.copy(date = intent.dateMillis)
            }
        }
    }
}