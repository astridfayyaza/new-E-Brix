package com.aryama0073.e_brix.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aryama0073.e_brix.ui.DetailScreen
import com.aryama0073.e_brix.ui.FormScreen
import com.aryama0073.e_brix.ui.HomeScreen
import com.aryama0073.e_brix.viewmodel.ScanViewModel

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val viewModel: ScanViewModel = viewModel()

    NavHost(navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate("form") },
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }

        composable("form") {
            FormScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("detail/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toInt() ?: 0

            DetailScreen(
                viewModel = viewModel,
                id = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}