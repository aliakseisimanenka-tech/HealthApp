package com.lsimanenka.healthapp.features.workout.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    }

    sealed interface SideEffect {
        object NavigateBack : SideEffect
    }

    object Reducer {
        fun reduce(state: State, intent: Intent): State {
            return when (intent) {
                is Intent.UpdateName -> state.copy(name = intent.name)
                is Intent.UpdateActionType -> state.copy(actionType = intent.type)
                Intent.OnCloseClick -> state
                Intent.OnSaveClick -> state
            }
        }
    }
}