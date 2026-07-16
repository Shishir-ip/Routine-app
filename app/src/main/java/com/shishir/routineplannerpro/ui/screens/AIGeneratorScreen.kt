package com.shishir.routineplannerpro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIGeneratorScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Generator") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("AI Routine Generator", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            Text("Coming soon: Generate JSON using OpenRouter API.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
