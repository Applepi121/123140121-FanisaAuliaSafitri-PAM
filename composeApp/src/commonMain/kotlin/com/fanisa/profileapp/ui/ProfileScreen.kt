package com.fanisa.profileapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fanisa.profileapp.data.ProfileUiState

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onNavigateToEdit: () -> Unit,
    onToggleDark: (Boolean) -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Profile Mahasiswa", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Text("Nama: ${uiState.name}")
            Text("NIM: ${uiState.nim}")
            Text("Bio: ${uiState.bio}")

            Spacer(modifier = Modifier.height(24.dp))
            // Navigasi Forward ke Edit Profile
            Button(onClick = onNavigateToEdit) {
                Text("Edit Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Dark Mode")
                Switch(checked = uiState.isDarkMode, onCheckedChange = onToggleDark)
            }
        }
    }
}