package com.lsimanenka.healthapp.features.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class HistoryViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        handle.get<HistoryContract.State>(KEY_VALUE) ?: HistoryContract.State()
    )
    val state: StateFlow<HistoryContract.State> = _uiState.asStateFlow()
    private val _effect = Channel<HistoryContract.SideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: HistoryContract.Intent) {
        updateState { currentState ->
            HistoryContract.Reducer.reduce(currentState, intent)
        }

        when (intent) {
            HistoryContract.Intent.OnAddWorkoutClick -> { sendEffect(HistoryContract.SideEffect.NavigateAtWorkout) }
            HistoryContract.Intent.RefreshData -> {}
        }
    }

    private fun sendEffect(effect: HistoryContract.SideEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun updateState(update: (HistoryContract.State) -> HistoryContract.State) {
        _uiState.update { current ->
            val updated = update(current)
            handle[KEY_VALUE] = updated
            updated
        }
    }

    companion object {
        private const val KEY_VALUE = "history_state"
    }
}