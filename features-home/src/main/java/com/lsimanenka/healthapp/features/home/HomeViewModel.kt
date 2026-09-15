package com.lsimanenka.healthapp.features.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val state = handle.getStateFlow(KEY_VALUE, HomeContract.State())

    init {
        if (state.value.steps == 0) {
            onIntent(HomeContract.Intent.RefreshData)
        }
    }

    fun onIntent(intent: HomeContract.Intent) {
        val newState = HomeContract.Reducer.reduce(state = state.value, intent = intent)
        handle[KEY_VALUE] = newState
    }

    companion object {
        private const val KEY_VALUE = "state"
    }
}