package com.lsimanenka.healthapp.features.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface HomeContract {

    @Parcelize
    data class State(
        val cardioPoints: Int = 0,
        val steps: Int = 0,
        val calories: Int = 0,
        val distanceKm: Double = 0.0,
        val activeMinutes: Int = 0,
        val isLoading: Boolean = false
    ) : Parcelable

    sealed class Intent {
        object RefreshData : Intent()
        object OnAddClick : Intent()
    }

    object Reducer {
        fun reduce(state: State, intent: Intent): State {
            return when (intent) {
                Intent.RefreshData -> state.copy(
                    cardioPoints = (0..50).random(),
                    steps = (0..10000).random(),
                    calories = (0..500).random(),
                    distanceKm = (0..10).random().toDouble(),
                    activeMinutes = (0..120).random()
                )
                Intent.OnAddClick -> state
            }
        }
    }
}