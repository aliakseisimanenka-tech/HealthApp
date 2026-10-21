package com.lsimanenka.auth.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface AuthContract {

    @Parcelize
    data class State(
        val email: String = "",
        val password: String = "",
        val isLoginMode: Boolean = true, // true - Вход, false - Регистрация
        val isLoading: Boolean = false
    ) : Parcelable

    sealed class Intent {
        data class UpdateEmail(val email: String) : Intent()
        data class UpdatePassword(val password: String) : Intent()
        object ToggleMode : Intent()
        object SubmitAuth : Intent()
        object OnBackClick : Intent()
    }

    sealed interface SideEffect {
        object NavigateBack : SideEffect
        data class ShowSnackbar(val message: String) : SideEffect
    }

    object Reducer {
        fun reduce(state: State, intent: Intent): State {
            return when (intent) {
                is Intent.UpdateEmail -> state.copy(email = intent.email)
                is Intent.UpdatePassword -> state.copy(password = intent.password)
                Intent.ToggleMode -> state.copy(isLoginMode = !state.isLoginMode)
                Intent.SubmitAuth -> state.copy(isLoading = true)
                Intent.OnBackClick -> state
            }
        }
    }
}