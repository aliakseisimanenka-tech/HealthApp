package com.lsimanenka.healthapp.features.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsimanenka.healthapp.features.home.domain.HomeRepository
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
class HomeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: HomeRepository,
    private val syncManager: WorkoutSyncManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        handle.get<HomeContract.State>(KEY_VALUE) ?: HomeContract.State()
    )
    val state: StateFlow<HomeContract.State> = _uiState.asStateFlow()
    private val _effect = Channel<HomeContract.SideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeMetrics()

        viewModelScope.launch {
            syncManager.sync()
        }
    }

    fun onIntent(intent: HomeContract.Intent) {
        updateState { currentState ->
            HomeContract.Reducer.reduce(currentState, intent)
        }

        when (intent) {
            HomeContract.Intent.LoadData -> {}
            HomeContract.Intent.OnAddClick -> { sendEffect(HomeContract.SideEffect.NavigateAtWorkout) }
            is HomeContract.Intent.DataLoaded -> {  }
        }
    }

    private fun sendEffect(effect: HomeContract.SideEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun observeMetrics() {
        viewModelScope.launch {
            repository.getDailyMetrics().collect { metrics ->
                onIntent(HomeContract.Intent.DataLoaded(metrics))
            }
        }
    }

    private fun updateState(update: (HomeContract.State) -> HomeContract.State) {
        _uiState.update { current ->
            val updated = update(current)
            handle[KEY_VALUE] = updated
            updated
        }
    }


    companion object {
        private const val KEY_VALUE = "state"
    }
}