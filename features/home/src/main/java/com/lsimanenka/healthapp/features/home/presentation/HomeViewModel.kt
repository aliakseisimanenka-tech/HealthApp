package com.lsimanenka.healthapp.features.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsimanenka.healthapp.features.home.domain.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: HomeRepository
) : ViewModel() {

    val state = handle.getStateFlow(KEY_VALUE, HomeContract.State())

    private val _effect = Channel<HomeContract.SideEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeMetrics()
    }

    fun onIntent(intent: HomeContract.Intent) {
        val newState = HomeContract.Reducer.reduce(state = state.value, intent = intent)
        handle[KEY_VALUE] = newState

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


    companion object {
        private const val KEY_VALUE = "state"
    }
}