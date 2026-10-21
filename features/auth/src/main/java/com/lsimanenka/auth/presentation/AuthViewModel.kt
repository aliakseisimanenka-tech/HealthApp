package com.lsimanenka.auth.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsimanenka.auth.domain.AuthRepository
import com.lsimanenka.shared.remote.WorkoutSyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val syncManager: WorkoutSyncManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        handle.get<AuthContract.State>(KEY_STATE) ?: AuthContract.State()
    )
    val state: StateFlow<AuthContract.State> = _uiState.asStateFlow()
    private val _effect = Channel<AuthContract.SideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: AuthContract.Intent) {
        updateState { currentState ->
            AuthContract.Reducer.reduce(currentState, intent)
        }

        when (intent) {
            AuthContract.Intent.SubmitAuth -> submitAuth()
            AuthContract.Intent.OnBackClick -> sendEffect(AuthContract.SideEffect.NavigateBack)
            else -> {}
        }
    }

    private fun submitAuth() {
        viewModelScope.launch {
            val currentState = state.value
            val email = currentState.email.trim()
            val password = currentState.password

            if (email.isBlank() || password.isBlank()) {
                updateState { it.copy(isLoading = false) }
                sendEffect(AuthContract.SideEffect.ShowSnackbar("Заполните почту и пароль"))
                return@launch
            }

            if (password.length < 8) {
                updateState { it.copy(isLoading = false) }
                sendEffect(AuthContract.SideEffect.ShowSnackbar("Пароль должен содержать минимум 8 символов"))
                return@launch
            }

            updateState { it.copy(isLoading = true) }

            val result = if (currentState.isLoginMode) {
                authRepository.login(email, password)
            } else {
                authRepository.register(email, password)
            }

            result.onSuccess {
                updateState { it.copy(isLoading = false, password = "") }
                viewModelScope.launch { syncManager.sync() }
                sendEffect(AuthContract.SideEffect.ShowSnackbar("Успешно!"))
                sendEffect(AuthContract.SideEffect.NavigateBack)
            }.onFailure { error ->
                updateState { it.copy(isLoading = false) }
                sendEffect(AuthContract.SideEffect.ShowSnackbar(error.message ?: "Ошибка авторизации"))
            }
        }
    }

    private fun sendEffect(effect: AuthContract.SideEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun updateState(update: (AuthContract.State) -> AuthContract.State) {
        _uiState.update { current ->
            val updated = update(current)
            handle[KEY_STATE] = updated
            updated
        }
    }

    companion object {
        private const val KEY_STATE = "auth_state"
    }
}