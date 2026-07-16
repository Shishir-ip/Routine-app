package com.shishir.routineplannerpro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
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
    var showAddRoutineDialog by remember { mutableStateOf(false) }

    LaunchedEffect(routines) {
        if (selectedRoutineId == null && routines.isNotEmpty()) selectedRoutineId = routines.first().id
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Routine Planner Pro", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedRoutineId != null) {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.ADD_ACTIVITY.replace("{routineId}", selectedRoutineId.toString())) },
                    containerColor = MaterialTheme.colorScheme.tertiary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Activity")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            DateSelector(selectedDate, onDateSelected = { selectedDate = it })
            
            if (routines.isNotEmpty()) {
                val selectedIndex = routines.indexOfFirst { it.id == selectedRoutineId }.coerceAtLeast(0)
                ScrollableTabRow(selectedTabIndex = selectedIndex, edgePadding = 16.dp) {
                    routines.forEachIndexed { index, routine ->
                        Tab(
                            selected = selectedIndex == index,
                            onClick = { selectedRoutineId = routine.id },
                            text = { Text(routine.name, fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                    Tab(selected = false, onClick = { showAddRoutineDialog = true }, icon = { Icon(Icons.Default.Add, "Add Routine") })
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CalendarMonth, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(Modifier.height(16.dp))
                        Text("No routines yet!", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { showAddRoutineDialog = true }) { Text("Create Routine") }
                    }
                }
            }

            val filteredActivities = activities.filter { it.routineId == selectedRoutineId }
            if (filteredActivities.isEmpty() && selectedRoutineId != null) {
                Box(Modifier.fillMaxSize().padding(32.dp), Alignment.Center) {
                    Text("No activities. Tap + to add one!", color = MaterialTheme.colorScheme.outline)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredActivities) { ActivityCard(it) }
                }
            }
        }
    }

    if (showAddRoutineDialog) {
        AddRoutineDialog(onDismiss = { showAddRoutineDialog = false }, onSave = { name, type ->
            viewModel.addRoutine(Routine(name = name, type = type))
            showAddRoutineDialog = false
        })
    }
}

@Composable
fun DateSelector(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val dates = remember { (-3..10).map { LocalDate.now().plusDays(it.toLong()) } }
    LazyRow(contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(dates) { date ->
            val isSelected = date == selectedDate
            Column(
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onDateSelected(date) }.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(date.format(DateTimeFormatter.ofPattern("EEE")), style = MaterialTheme.typography.labelSmall, color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                Text(date.dayOfMonth.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun AddRoutineDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Daily") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Routine") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                Row {
                    FilterChip(type == "Daily", { type = "Daily" }, { Text("Daily") })
                    Spacer(Modifier.width(8.dp))
                    FilterChip(type == "Class", { type = "Class" }, { Text("Class") })
                    Spacer(Modifier.width(8.dp))
                    FilterChip(type == "Custom", { type = "Custom" }, { Text("Custom") })
                }
            }
        },
        confirmButton = { TextButton(onClick = { if (name.isNotBlank()) onSave(name, type) }) { Text("Create") } },
        dismissButton = { TextButton(onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun ActivityCard(activity: Activity) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column {
                    Text(activity.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${activity.startTime} - ${activity.endTime}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
                if (activity.alarmEnabled || activity.reminderEnabled) Icon(Icons.Default.Alarm, "Alarm", tint = MaterialTheme.colorScheme.tertiary)
            }
            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(top = 12.dp)) {
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    Text("Days: ${activity.daysOfWeek}", style = MaterialTheme.typography.bodySmall)
                    Text("Details: ${activity.detailsJson}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
