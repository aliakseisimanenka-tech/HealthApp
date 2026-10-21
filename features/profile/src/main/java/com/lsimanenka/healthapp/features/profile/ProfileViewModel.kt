package com.lsimanenka.healthapp.features.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsimanenka.auth.domain.AuthRepository
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
class ProfileViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        handle.get<ProfileContract.State>(KEY_VALUE) ?: ProfileContract.State()
    )
    val state: StateFlow<ProfileContract.State> = _uiState.asStateFlow()

    private val _effect = Channel<ProfileContract.SideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeAuthState()
    }

    fun onIntent(intent: ProfileContract.Intent) {
        updateState { currentState ->
            ProfileContract.Reducer.reduce(currentState, intent)
        }

        when (intent) {
            ProfileContract.Intent.OnAuthClick -> {
                sendEffect(ProfileContract.SideEffect.NavigateToAuth)
            }
            ProfileContract.Intent.OnLogoutClick -> {
                logout()
            }
            else -> {}
        }
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.currentUserEmail.collect { email ->
                updateState { it.copy(userEmail = email) }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            sendEffect(ProfileContract.SideEffect.ShowSnackbar("Вы вышли из аккаунта"))
        }
    }

    private fun sendEffect(effect: ProfileContract.SideEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun updateState(update: (ProfileContract.State) -> ProfileContract.State) {
        _uiState.update { current ->
            val updated = update(current)
            handle[KEY_VALUE] = updated
            updated
        }
    }

    companion object {
        private const val KEY_VALUE = "profile_state"
    }
}