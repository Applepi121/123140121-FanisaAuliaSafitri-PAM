package com.fanisa.profileapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.LabeledTextField
import viewmodel.ProfileViewModel

@Composable
fun ProfileHeader(name: String, bio: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Menampilkan nama dan bio secara reaktif dari state
        Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = bio, color = MaterialTheme.colorScheme.secondary, fontSize = 16.sp)
    }
}

@Composable
fun App() {
    // 1. Inisialisasi ViewModel
    val viewModel: ProfileViewModel = viewModel { ProfileViewModel() }

    // 2. Collect UI State secara reaktif
    val uiState by viewModel.uiState.collectAsState()

    // 3. Implementasi Tema Dinamis (Dark Mode)
    MaterialTheme(
        colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Mengirim data dari uiState ke Header
                        ProfileHeader(uiState.name, uiState.bio)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        // Form Edit dengan State Hoisting
                        Text("Edit Profile Information", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

                        LabeledTextField(
                            label = "Full Name",
                            value = uiState.name,
                            onValueChange = { viewModel.updateName(it) } // Event naik ke ViewModel
                        )

                        LabeledTextField(
                            label = "Bio",
                            value = uiState.bio,
                            onValueChange = { viewModel.updateBio(it) } // Event naik ke ViewModel
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Switch untuk Dark Mode
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Dark Mode", fontWeight = FontWeight.Medium)
                            Switch(
                                checked = uiState.isDarkMode,
                                onCheckedChange = { viewModel.toggleDarkMode(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}