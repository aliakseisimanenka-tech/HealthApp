package com.lsimanenka.healthapp.features.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val state = handle.getStateFlow(KEY_VALUE, HistoryContract.State())

    private val _effect = Channel<HistoryContract.SideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: HistoryContract.Intent) {
        val newState = HistoryContract.Reducer.reduce(state = state.value, intent = intent)
        handle[KEY_VALUE] = newState

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

    companion object {
        private const val KEY_VALUE = "history_state"
    }
}