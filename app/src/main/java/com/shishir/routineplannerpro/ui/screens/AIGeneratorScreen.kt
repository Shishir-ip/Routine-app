
package com.shishir.routineplannerpro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIGeneratorScreen(navController: NavController) {
    var prompt by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("AI Generator") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Describe your routine (OpenRouter model=openrouter/free):", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                label = { Text("e.g., Studying from 10 am to 12 pm...") }
            )
            Button(onClick = {
                // Retrofit API call goes here
                result = "JSON Output will appear here to import..."
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Generate JSON")
            }
            
            if (result.isNotEmpty()) {
                Card {
                    Text(result, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
