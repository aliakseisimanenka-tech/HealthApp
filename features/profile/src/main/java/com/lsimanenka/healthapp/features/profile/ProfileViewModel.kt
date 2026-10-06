package com.lsimanenka.healthapp.features.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val state = handle.getStateFlow(KEY_VALUE, ProfileContract.State())

    fun onIntent(intent: ProfileContract.Intent) {
        val newState = ProfileContract.Reducer.reduce(state = state.value, intent = intent)
        handle[KEY_VALUE] = newState
    }

    companion object {
        private const val KEY_VALUE = "profile_state"
    }
}