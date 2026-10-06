package com.lsimanenka.healthapp.features.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lsimanenka.healthapp.core.ui.theme.FitBlue
import com.lsimanenka.healthapp.core.ui.theme.FitTeal
import com.lsimanenka.healthapp.features.home.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToAddWorkout: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                HomeContract.SideEffect.NavigateAtWorkout -> {
                    onNavigateToAddWorkout()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(HomeContract.Intent.OnAddClick) },
                containerColor = Color.White,
                contentColor = FitBlue
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.home_fab_add_desc)
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(150.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MainMetricItem(
                    label = stringResource(R.string.home_cardio_points),
                    value = state.cardioPoints.toString(),
                    icon = Icons.Default.FavoriteBorder,
                    color = FitTeal
                )
                MainMetricItem(
                    label = stringResource(R.string.home_steps),
                    value = state.steps.toString(),
                    icon = Icons.Default.DirectionsWalk,
                    color = FitBlue
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SecondaryMetricItem(
                    value = state.calories.toString(),
                    label = stringResource(R.string.home_kcal)
                )
                SecondaryMetricItem(
                    value = state.distanceKm.toString(),
                    label = stringResource(R.string.home_km)
                )
                SecondaryMetricItem(
                    value = state.activeMinutes.toString(),
                    label = stringResource(R.string.home_active_minutes)
                )
            }
        }
    }
}

@Composable
fun MainMetricItem(label: String, value: String, icon: ImageVector, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SecondaryMetricItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.headlineSmall, color = FitBlue)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}