package com.lsimanenka.healthapp.features.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsimanenka.healthapp.features.home.domain.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: HomeRepository
) : ViewModel() {

    val state = handle.getStateFlow(KEY_VALUE, HomeContract.State())

    init {
        if (state.value.steps == 0) {
            onIntent(HomeContract.Intent.LoadData)
        }
    }

    fun onIntent(intent: HomeContract.Intent) {
        val newState = HomeContract.Reducer.reduce(state = state.value, intent = intent)
        handle[KEY_VALUE] = newState

        when (intent) {
            HomeContract.Intent.LoadData -> loadMetrics()
            HomeContract.Intent.OnAddClick -> {  }
            is HomeContract.Intent.DataLoaded -> {  }
        }
    }

    private fun loadMetrics() {
        viewModelScope.launch {
            val metrics = repository.getDailyMetrics()

            onIntent(HomeContract.Intent.DataLoaded(metrics))
        }
    }


    companion object {
        private const val KEY_VALUE = "state"
    }
}