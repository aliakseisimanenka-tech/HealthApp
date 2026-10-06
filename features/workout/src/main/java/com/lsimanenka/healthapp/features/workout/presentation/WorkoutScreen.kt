package com.lsimanenka.healthapp.features.workout.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AddWorkoutContract.SideEffect.NavigateBack -> {
                    onClose()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onIntent(AddWorkoutContract.Intent.OnCloseClick)
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    Text(
                        text = "Сохранить",
                        color = Color(0xFF1A73E8),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable { viewModel.onIntent(AddWorkoutContract.Intent.OnSaveClick) }
                            .padding(horizontal = 8.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Добавить\nактивность",
                style = MaterialTheme.typography.displaySmall,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            WorkoutFieldItem(
                label = "Название",
                value = state.name.ifEmpty { "Добавить" },
                isValuePlaceholder = state.name.isEmpty()
            )
            WorkoutFieldItem(
                label = "Действия",
                value = state.actionType
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            WorkoutFieldItem(
                label = "Начало",
                value = "${state.date}    ${state.time}"
            )
            WorkoutFieldItem(
                label = "Продолжительность",
                value = "${state.durationMin} мин."
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            WorkoutFieldItem(
                label = "Интенсивность",
                value = state.cardioPoints.ifEmpty { "Добавить параметр \"баллы кардио\"" },
                isValuePlaceholder = state.cardioPoints.isEmpty()
            )
            WorkoutFieldItem(
                label = "Расстояние",
                value = state.distance.ifEmpty { "Добавить параметр \"км\"" },
                isValuePlaceholder = state.distance.isEmpty()
            )
            WorkoutFieldItem(
                label = "Расход энергии",
                value = state.calories.ifEmpty { "Добавить параметр \"ккал\"" },
                isValuePlaceholder = state.calories.isEmpty()
            )
            WorkoutFieldItem(
                label = "Шаги",
                value = state.steps.ifEmpty { "Добавить параметр \"шаги\"" },
                isValuePlaceholder = state.steps.isEmpty()
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            // Поле для заметок
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Открыть редактор заметок */ }
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Text(
                    text = state.notes.ifEmpty { "Добавьте заметки" },
                    color = if (state.notes.isEmpty()) Color.Gray else Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun WorkoutFieldItem(
    label: String,
    value: String,
    isValuePlaceholder: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {  }
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isValuePlaceholder) Color.Gray else Color.Black
        )
    }
}