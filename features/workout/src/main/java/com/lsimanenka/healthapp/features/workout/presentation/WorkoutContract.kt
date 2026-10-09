package com.lsimanenka.healthapp.features.workout.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface AddWorkoutContract {

    @Parcelize
    data class State(
        val name: String = "",
        val actionType: String = "Ходьба",
        val date: String = "Сегодня",
        val time: String = "10:54",
        val durationMin: String = "30",
        val cardioPoints: String = "",
        val distance: String = "",
        val calories: String = "",
        val steps: String = "",
        val notes: String = ""
    ) : Parcelable

    sealed class Intent {
        object OnCloseClick : Intent()
        object OnSaveClick : Intent()

        data class UpdateName(val name: String) : Intent()
        data class UpdateActionType(val type: String) : Intent()
        data class UpdateDate(val dateMillis: Long) : Intent()
        data class UpdateTime(val time: String) : Intent()
        data class UpdateDuration(val duration: String) : Intent()
        data class UpdateCardio(val cardio: String) : Intent()
        data class UpdateDistance(val distance: String) : Intent()
        data class UpdateCalories(val calories: String) : Intent()
        data class UpdateSteps(val steps: String) : Intent()
        data class UpdateNotes(val notes: String) : Intent()
    }

    sealed interface SideEffect {
        object NavigateBack : SideEffect
    }

    object Reducer {
        fun reduce(state: State, intent: Intent): State {
            return when (intent) {
                Intent.OnCloseClick -> state
                Intent.OnSaveClick -> state

                is Intent.UpdateName -> state.copy(name = intent.name)
                is Intent.UpdateActionType -> state.copy(actionType = intent.type)
                is Intent.UpdateTime -> state.copy(time = intent.time)
                is Intent.UpdateDuration -> state.copy(durationMin = intent.duration)
                is Intent.UpdateCardio -> state.copy(cardioPoints = intent.cardio)
                is Intent.UpdateDistance -> state.copy(distance = intent.distance)
                is Intent.UpdateCalories -> state.copy(calories = intent.calories)
                is Intent.UpdateSteps -> state.copy(steps = intent.steps)
                is Intent.UpdateNotes -> state.copy(notes = intent.notes)

                is Intent.UpdateDate -> {
                    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val dateString = formatter.format(Date(intent.dateMillis))
                    state.copy(date = dateString)
                }
            }
        }
    }
}