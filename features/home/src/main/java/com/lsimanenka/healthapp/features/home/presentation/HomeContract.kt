package com.lsimanenka.healthapp.features.home.presentation

import android.os.Parcelable
import com.lsimanenka.healthapp.features.home.domain.DailyMetrics
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
        object LoadData : Intent()
        data class DataLoaded(val metrics: DailyMetrics) : Intent()
        object OnAddClick : Intent()
    }

    sealed interface SideEffect {
        object NavigateAtWorkout : SideEffect
    }

    object Reducer {
        fun reduce(state: State, intent: Intent): State {
            return when (intent) {
                Intent.LoadData -> state.copy(isLoading = true)

                is Intent.DataLoaded -> state.copy(
                    cardioPoints = intent.metrics.cardioPoints,
                    steps = intent.metrics.steps,
                    calories = intent.metrics.calories,
                    distanceKm = intent.metrics.distanceKm,
                    activeMinutes = intent.metrics.activeMinutes,
                    isLoading = false,
                )
                Intent.OnAddClick -> state
            }
        }
    }
}