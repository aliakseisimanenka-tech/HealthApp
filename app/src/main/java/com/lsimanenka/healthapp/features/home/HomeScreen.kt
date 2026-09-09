package com.lsimanenka.healthapp.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(HomeContract.Intent.OnAddClick) },
                containerColor = Color.White,
                contentColor = Color(0xFF4285F4)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
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
                    label = "Баллы кардио:",
                    value = state.cardioPoints.toString(),
                    icon = Icons.Default.FavoriteBorder,
                    color = Color(0xFF00C4B4)
                )
                MainMetricItem(
                    label = "Шаги:",
                    value = state.steps.toString(),
                    icon = Icons.Default.DirectionsWalk,
                    color = Color(0xFF4285F4)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SecondaryMetricItem(value = state.calories.toString(), label = "ккал")
                SecondaryMetricItem(value = state.distanceKm.toString(), label = "км")
                SecondaryMetricItem(value = state.activeMinutes.toString(), label = "мин. активности")
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
        Text(text = value, style = MaterialTheme.typography.headlineSmall, color = Color(0xFF4285F4))
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}