package com.lsimanenka.healthapp.features.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val state = handle.getStateFlow(KEY_VALUE, HistoryContract.State())

    fun onIntent(intent: HistoryContract.Intent) {
        val newState = HistoryContract.Reducer.reduce(state = state.value, intent = intent)
        handle[KEY_VALUE] = newState
    }

    companion object {
        private const val KEY_VALUE = "history_state"
    }
}