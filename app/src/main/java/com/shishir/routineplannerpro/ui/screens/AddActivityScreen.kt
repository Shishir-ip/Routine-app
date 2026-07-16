
package com.shishir.routineplannerpro.ui.screens

import androidx.compose.foundation.layout.*
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
    var name by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("12:00") }
    var endTime by remember { mutableStateOf("13:00") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Activity") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Activity Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = startTime, onValueChange = { startTime = it }, label = { Text("Start Time") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = endTime, onValueChange = { endTime = it }, label = { Text("End Time") }, modifier = Modifier.fillMaxWidth())
            
            Button(onClick = {
                val activity = Activity(routineId = routineId, name = name, startTime = startTime, endTime = endTime, daysOfWeek = "Every Day")
                viewModel.addActivity(activity)
                navController.popBackStack()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Save Activity")
            }
        }
    }
}
