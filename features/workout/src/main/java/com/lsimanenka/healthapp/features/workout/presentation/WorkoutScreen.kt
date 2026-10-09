package com.lsimanenka.healthapp.features.workout.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lsimanenka.healthapp.features.workout.presentation.util.WorkoutAction
import com.lsimanenka.healthapp.features.workout.presentation.util.WorkoutClickableFieldItem
import com.lsimanenka.healthapp.features.workout.presentation.util.WorkoutEditableFieldItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showActionDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AddWorkoutContract.SideEffect.NavigateBack -> onClose()
            }
        }
    }

    if (showActionDialog) {
        ActionDropdownDialog(
            onDismiss = { showActionDialog = false },
            onSelect = { actionName ->
                viewModel.onIntent(AddWorkoutContract.Intent.UpdateActionType(actionName))
                showActionDialog = false
            }
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                val formattedTime = String.format("%02d:%02d", hour, minute)
                viewModel.onIntent(AddWorkoutContract.Intent.UpdateTime(formattedTime))
                showTimePicker = false
            }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onIntent(AddWorkoutContract.Intent.UpdateDate(millis))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Отмена") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(AddWorkoutContract.Intent.OnCloseClick) }) {
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

            WorkoutEditableFieldItem(
                label = "Название",
                value = state.name,
                placeholder = "Добавить",
                onValueChange = { viewModel.onIntent(AddWorkoutContract.Intent.UpdateName(it)) }
            )

            WorkoutClickableFieldItem(
                label = "Действия",
                value = state.actionType,
                onClick = { showActionDialog = true }
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Начало",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    Text(
                        text = state.date,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = state.time,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable { showTimePicker = true }
                    )
                }
            }

            WorkoutEditableFieldItem(
                label = "Продолжительность",
                value = state.durationMin,
                placeholder = "30",
                onValueChange = { viewModel.onIntent(AddWorkoutContract.Intent.UpdateDuration(it)) },
                keyboardType = KeyboardType.Number
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            WorkoutEditableFieldItem(
                label = "Интенсивность",
                value = state.cardioPoints,
                placeholder = "Добавить параметр \"баллы кардио\"",
                onValueChange = { viewModel.onIntent(AddWorkoutContract.Intent.UpdateCardio(it)) },
                keyboardType = KeyboardType.Number
            )
            WorkoutEditableFieldItem(
                label = "Расстояние",
                value = state.distance,
                placeholder = "Добавить параметр \"км\"",
                onValueChange = { viewModel.onIntent(AddWorkoutContract.Intent.UpdateDistance(it)) },
                keyboardType = KeyboardType.Number
            )
            WorkoutEditableFieldItem(
                label = "Расход энергии",
                value = state.calories,
                placeholder = "Добавить параметр \"ккал\"",
                onValueChange = { viewModel.onIntent(AddWorkoutContract.Intent.UpdateCalories(it)) },
                keyboardType = KeyboardType.Number
            )
            WorkoutEditableFieldItem(
                label = "Шаги",
                value = state.steps,
                placeholder = "Добавить параметр \"шаги\"",
                onValueChange = { viewModel.onIntent(AddWorkoutContract.Intent.UpdateSteps(it)) },
                keyboardType = KeyboardType.Number
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            BasicTextField(
                value = state.notes,
                onValueChange = { viewModel.onIntent(AddWorkoutContract.Intent.UpdateNotes(it)) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                decorationBox = { innerTextField ->
                    if (state.notes.isEmpty()) {
                        Text(
                            text = "Добавьте заметки",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    innerTextField()
                }
            )

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    }
}


@Composable
fun ActionDropdownDialog(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите действие") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                WorkoutAction.entries.forEach { action ->
                    val actionName = stringResource(action.titleResId)

                    Text(
                        text = actionName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(actionName) }
                            .padding(vertical = 12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        confirmButton = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val timePickerState = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        text = { TimePicker(state = timePickerState) },
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}