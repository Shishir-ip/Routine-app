
package com.shishir.routineplannerpro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shishir.routineplannerpro.RoutineViewModel
import com.shishir.routineplannerpro.data.Activity
import com.shishir.routineplannerpro.data.Routine
import com.shishir.routineplannerpro.ui.navigation.Routes
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: RoutineViewModel = viewModel()) {
    val routines by viewModel.routines.collectAsState(initial = emptyList())
    val activities by viewModel.allActivities.collectAsState(initial = emptyList())
    
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedRoutineId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Routine Planner Pro") },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.AI_GENERATOR) }) {
                        Icon(Icons.Default.SmartToy, contentDescription = "AI Generator")
                    }
                    IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedRoutineId != null) {
                FloatingActionButton(onClick = { navController.navigate(Routes.ADD_ACTIVITY.replace("{routineId}", selectedRoutineId.toString())) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Activity")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Calendar Date Selector (Visual Placeholder)
            Text("Date: ${selectedDate.format(DateTimeFormatter.ISO_DATE)}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // Routines Tabs
            ScrollableTabRow(selectedTabIndex = routines.indexOfFirst { it.id == selectedRoutineId }.coerceAtLeast(0)) {
                routines.forEach { routine ->
                    Tab(
                        selected = selectedRoutineId == routine.id,
                        onClick = { selectedRoutineId = routine.id },
                        text = { Text(routine.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Activities List (Unified View logic)
            val filteredActivities = activities.filter { 
                selectedRoutineId == null || it.routineId == selectedRoutineId 
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredActivities) { activity ->
                    ActivityCard(activity, routines.find { it.id == activity.routineId }?.name)
                }
            }
        }
    }
}

@Composable
fun ActivityCard(activity: Activity, routineName: String?) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = activity.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "${activity.startTime} - ${activity.endTime}", style = MaterialTheme.typography.bodySmall)
            }
            if (routineName != null) {
                Text(text = "From: $routineName", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
            
            // Expand with nice animation
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text("Days: ${activity.daysOfWeek}", style = MaterialTheme.typography.bodySmall)
                    Text("Details: ${activity.detailsJson}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
