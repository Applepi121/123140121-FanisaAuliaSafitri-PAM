package com.fanisa.profileapp

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.compose.material3.*
import com.fanisa.profileapp.viewmodel.ProfileViewModel
import com.fanisa.profileapp.navigation.Screen
import com.fanisa.profileapp.ui.*

@Composable
fun App() {
    val navController = rememberNavController()
    val viewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme(
        colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Profile.route
        ) {
            // Destinasi 1: Profile [cite: 154]
            composable(route = Screen.Profile.route) {
                ProfileScreen(
                    uiState = uiState,
                    onNavigateToEdit = {
                        navController.navigate(Screen.EditProfile.route) // Pindah layar [cite: 167]
                    },
                    onToggleDark = { viewModel.toggleDarkMode(it) }
                )
            }
            // Destinasi 2: Edit Profile [cite: 162]
            composable(route = Screen.EditProfile.route) {
                EditProfileScreen(
                    uiState = uiState,
                    onNameChange = { viewModel.updateName(it) },
                    onBioChange = { viewModel.updateBio(it) },
                    onBack = {
                        navController.popBackStack() // Kembali ke home [cite: 169]
                    }
                )
            }
        }
    }
}