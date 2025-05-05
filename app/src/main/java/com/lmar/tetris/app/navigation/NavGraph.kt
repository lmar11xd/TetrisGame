package com.lmar.tetris.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lmar.tetris.presentation.game.GameScreen
import com.lmar.tetris.presentation.home.HomeScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Game : Screen("game")
}

@Composable
fun NavGraph(startDestination: String = Screen.Home.route) {
    val navController = rememberNavController()

    NavHost(navController, startDestination) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Game.route) {
            GameScreen(navController)
        }
    }
}