package com.fanisa.profileapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fanisa.profileapp.data.ProfileUiState

@Composable
fun EditProfileScreen(
    uiState: ProfileUiState,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Edit Profile", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = uiState.bio,
                onValueChange = onBioChange,
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            // Navigasi Kembali ke Profile
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Save & Back")
            }
        }
    }
}