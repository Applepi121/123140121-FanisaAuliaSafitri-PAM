package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel { ProfileViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme(
        colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "My Profile", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

                Text(text = "Name: ${uiState.name}", fontSize = 18.sp)
                Text(text = "Bio: ${uiState.bio}", fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)

                Spacer(modifier = Modifier.height(24.dp))

                LabeledTextField(
                    label = "Edit Name",
                    value = uiState.name,
                    onValueChange = { viewModel.updateName(it) }
                )

                LabeledTextField(
                    label = "Edit Bio",
                    value = uiState.bio,
                    onValueChange = { viewModel.updateBio(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Dark Mode")
                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode(it) }
                    )
                }
            }
        }
    }
}