package com.aryama0073.e_brix.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aryama0073.e_brix.ui.DetailScreen
import com.aryama0073.e_brix.ui.FormScreen
import com.aryama0073.e_brix.ui.HomeScreen
import com.aryama0073.e_brix.ui.LoginScreen
import com.aryama0073.e_brix.viewmodel.AuthViewModel
import com.aryama0073.e_brix.viewmodel.ScanViewModel

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val viewModel: ScanViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    val currentUser by authViewModel.currentUser.collectAsState()
    val startDestination = if (currentUser != null) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                authViewModel = authViewModel,
                onAddClick = { navController.navigate("form") },
                onEditClick = { id -> navController.navigate("form/edit/$id") },
                onItemClick = { id -> navController.navigate("detail/$id") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("form") {
            FormScreen(
                viewModel = viewModel,
                editId = null,
                onBack = { navController.popBackStack() }
            )
        }

        composable("form/edit/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toInt() ?: 0

            FormScreen(
                viewModel = viewModel,
                editId = id,
                onBack = { navController.popBackStack() }
            )
        }

        composable("detail/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toInt() ?: 0

            DetailScreen(
                viewModel = viewModel,
                id = id,
                onEditClick = { editId -> navController.navigate("form/edit/$editId") },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
