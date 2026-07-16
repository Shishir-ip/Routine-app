package com.shishir.routineplannerpro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shishir.routineplannerpro.ui.screens.HomeScreen
import com.shishir.routineplannerpro.ui.screens.SettingsScreen
import com.shishir.routineplannerpro.ui.screens.AddActivityScreen
import com.shishir.routineplannerpro.ui.screens.AIGeneratorScreen

object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val ADD_ACTIVITY = "add_activity/{routineId}"
    const val AI_GENERATOR = "ai_generator"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.SETTINGS) { SettingsScreen(navController) }
        composable(Routes.ADD_ACTIVITY) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("routineId")?.toLongOrNull() ?: 0L
            AddActivityScreen(navController, routineId)
        }
        composable(Routes.AI_GENERATOR) { AIGeneratorScreen(navController) }
    }
}
