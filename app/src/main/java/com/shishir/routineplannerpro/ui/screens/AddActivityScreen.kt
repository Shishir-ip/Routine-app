package com.shishir.routineplannerpro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shishir.routineplannerpro.RoutineViewModel
import com.shishir.routineplannerpro.data.Activity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(navController: NavController, routineId: Long, viewModel: RoutineViewModel = viewModel()) {
    val routines by viewModel.routines.collectAsState(initial = emptyList())
    val routine = routines.find { it.id == routineId }
    
    var name by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("10:00") }
    var room by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var reminderEnabled by remember { mutableStateOf(false) }
    var alarmEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add ${routine?.type ?: ""} Activity") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(if (routine?.type == "Class") "Course Name" else "Activity Name") }, modifier = Modifier.fillMaxWidth())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = startTime, onValueChange = { startTime = it }, label = { Text("Start (HH:MM)") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = endTime, onValueChange = { endTime = it }, label = { Text("End (HH:MM)") }, modifier = Modifier.weight(1f))
            }
            if (routine?.type == "Class") {
                OutlinedTextField(value = room, onValueChange = { room = it }, label = { Text("Room No.") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = teacher, onValueChange = { teacher = it }, label = { Text("Teacher Name") }, modifier = Modifier.fillMaxWidth())
            }
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Text("Reminder (5 min before)", style = MaterialTheme.typography.titleMedium)
                        Switch(checked = reminderEnabled, onCheckedChange = { reminderEnabled = it })
                    }
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Text("Alarm (Force Ring)", style = MaterialTheme.typography.titleMedium)
                        Switch(checked = alarmEnabled, onCheckedChange = { alarmEnabled = it })
                    }
                }
            }
            Button(
                onClick = {
                    val details = if (routine?.type == "Class") "{\"Room\": \"$room\", \"Teacher\": \"$teacher\"}" else "{}"
                    viewModel.addActivity(Activity(routineId = routineId, name = name, startTime = startTime, endTime = endTime, daysOfWeek = "Every Day", reminderEnabled = reminderEnabled, alarmEnabled = alarmEnabled, detailsJson = details))
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = name.isNotBlank()
            ) { Text("Save Activity", style = MaterialTheme.typography.titleMedium) }
        }
    }
}
