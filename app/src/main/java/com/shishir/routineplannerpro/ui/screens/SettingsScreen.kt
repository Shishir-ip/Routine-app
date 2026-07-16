
package com.shishir.routineplannerpro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shishir.routineplannerpro.ui.theme.RoutinePlannerProTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Theme", style = MaterialTheme.typography.titleLarge)
            // Theme toggle logic here
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("AI Generator API Key", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = "*************", 
                onValueChange = {}, 
                label = { Text("OpenRouter API Key") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("Developer Info", style = MaterialTheme.typography.titleLarge)
            Text("Created by Shishir")
            Text("GitHub: https://github.com/Shishir-ip")
        }
    }
}
