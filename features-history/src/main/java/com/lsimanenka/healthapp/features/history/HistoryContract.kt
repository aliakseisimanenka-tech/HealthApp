package com.lsimanenka.healthapp.features.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface HistoryContract {

    @Parcelize
    data class State(
        val hasWorkouts: Boolean = false,
        val isLoading: Boolean = false
    ) : Parcelable

    sealed class Intent {
        object RefreshData : Intent()
        object OnAddWorkoutClick : Intent()
    }

    object Reducer {
        fun reduce(state: State, intent: Intent): State {
            return when (intent) {
                Intent.RefreshData -> state
                Intent.OnAddWorkoutClick -> state
            }
        }
    }
}