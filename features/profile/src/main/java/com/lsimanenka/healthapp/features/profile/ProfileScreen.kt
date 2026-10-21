package com.lsimanenka.healthapp.features.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProfileContract.SideEffect.NavigateToAuth -> onNavigateToAuth()
                is ProfileContract.SideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { viewModel.onIntent(ProfileContract.Intent.OnSettingsClick) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = Color(0xFF8AB4F8)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = state.userEmail?.take(1)?.uppercase() ?: "А",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Профиль",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )

            AuthCard(state = state, onIntent = viewModel::onIntent)

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Фитнес-цели",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedDropdownField(
                        modifier = Modifier.weight(1f),
                        label = "Шаги",
                        value = state.stepsGoal.toString()
                    )
                    OutlinedDropdownField(
                        modifier = Modifier.weight(1f),
                        label = "Баллы кардиотренировки",
                        value = state.cardioGoal.toString()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Расписание режима сна",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Switch(
                    checked = state.sleepScheduleEnabled,
                    onCheckedChange = { viewModel.onIntent(ProfileContract.Intent.ToggleSleepSchedule(it)) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedDropdownField(
                    modifier = Modifier.weight(1f),
                    label = "Отход ко сну",
                    value = state.sleepTime
                )
                OutlinedDropdownField(
                    modifier = Modifier.weight(1f),
                    label = "Пробуждение",
                    value = state.wakeTime
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Ваши данные",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedDropdownField(
                        modifier = Modifier.weight(1f),
                        label = "Пол",
                        value = state.gender
                    )
                    OutlinedDropdownField(
                        modifier = Modifier.weight(1f),
                        label = "День рождения",
                        value = state.birthDate
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedDropdownField(
                        modifier = Modifier.weight(1f),
                        label = "Вес",
                        value = state.weight.ifEmpty { "Вес" }
                    )
                    OutlinedDropdownField(
                        modifier = Modifier.weight(1f),
                        label = "Рост",
                        value = state.height.ifEmpty { "Рост" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AuthCard(
    state: ProfileContract.State,
    onIntent: (ProfileContract.Intent) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF1F3F4)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.userEmail == null) {
                Text(
                    text = "Сохраните свои данные в облаке",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = "Войдите или создайте аккаунт, чтобы не потерять историю тренировок.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Button(
                    onClick = { onIntent(ProfileContract.Intent.OnAuthClick) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Войти / Зарегистрироваться")
                }
            } else {
                Text(
                    text = "Вы вошли как:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = state.userEmail,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                OutlinedButton(
                    onClick = { onIntent(ProfileContract.Intent.OnLogoutClick) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Выйти из аккаунта", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun OutlinedDropdownField(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Surface(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(y = (-6).dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}