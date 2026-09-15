package com.lsimanenka.healthapp.features.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface ProfileContract {

    @Parcelize
    data class State(
        val stepsGoal: Int = 5000,
        val cardioGoal: Int = 20,
        val sleepScheduleEnabled: Boolean = false,
        val sleepTime: String = "23:00",
        val wakeTime: String = "07:00",
        val gender: String = "Мужской",
        val birthDate: String = "11 окт. 1990 г.",
        val weight: String = "",
        val height: String = ""
    ) : Parcelable

    sealed class Intent {
        object OnSettingsClick : Intent()
        object OnProfileAvatarClick : Intent()
        data class UpdateStepsGoal(val steps: Int) : Intent()
        data class UpdateCardioGoal(val points: Int) : Intent()
        data class ToggleSleepSchedule(val enabled: Boolean) : Intent()
    }

    object Reducer {
        fun reduce(state: State, intent: Intent): State {
            return when (intent) {
                Intent.OnSettingsClick -> state
                Intent.OnProfileAvatarClick -> state
                is Intent.UpdateStepsGoal -> state.copy(stepsGoal = intent.steps)
                is Intent.UpdateCardioGoal -> state.copy(cardioGoal = intent.points)
                is Intent.ToggleSleepSchedule -> state.copy(sleepScheduleEnabled = intent.enabled)
            }
        }
    }
}