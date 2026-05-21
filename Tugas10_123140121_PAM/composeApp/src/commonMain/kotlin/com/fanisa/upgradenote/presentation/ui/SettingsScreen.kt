package com.fanisa.upgradenote.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanisa.upgradenote.data.platform.DeviceInfo
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: NotesViewModel,
    onNavigateBack: () -> Unit
) {
    val currentTheme by viewModel.theme.collectAsState()
    val isDescending by viewModel.isSortDescending.collectAsState()
    val deviceInfo: DeviceInfo = koinInject()
    val maroon = Color(0xFF800000)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = maroon,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SettingSectionCard(title = "Tema Aplikasi", icon = Icons.Default.Palette) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ThemeOption(color = Color(0xFF800000), label = "Maroon", isSelected = currentTheme == "maroon") {
                        viewModel.changeTheme("maroon")
                    }
                    ThemeOption(color = Color.Black, label = "Black", isSelected = currentTheme == "black") {
                        viewModel.changeTheme("black")
                    }
                    ThemeOption(color = Color(0xFFFFC0CB), label = "Pink", isSelected = currentTheme == "pink") {
                        viewModel.changeTheme("pink")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingSectionCard(title = "Urutan Catatan", icon = Icons.AutoMirrored.Filled.Sort) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SortOption(
                        name = "Terbaru (Descending)",
                        selected = isDescending,
                        onSelect = { viewModel.changeSortOrder(true) }
                    )
                    SortOption(
                        name = "Terlama (Ascending)",
                        selected = !isDescending,
                        onSelect = { viewModel.changeSortOrder(false) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingSectionCard(title = "Informasi Perangkat", icon = Icons.Default.Info) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    DeviceDetailRow(label = "Model", value = deviceInfo.getDeviceName())
                    DeviceDetailRow(label = "Versi OS", value = deviceInfo.getOsVersion())
                    DeviceDetailRow(label = "Versi Aplikasi", value = deviceInfo.getAppVersion())
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("UpgradeNote v${deviceInfo.getAppVersion()}", color = Color.Gray, fontSize = 12.sp)
                Text("Fanisa Aulia Safitri - 123140121", color = maroon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("Teknik Informatika ITERA", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DeviceDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SettingSectionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)) {
            Icon(icon, contentDescription = null, tint = Color(0xFF800000), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleSmall, color = Color(0xFF800000), fontWeight = FontWeight.Bold)
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) { content() }
        }
    }
}

@Composable
fun ThemeOption(color: Color, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(color)
                .border(if (isSelected) 3.dp else 0.dp, Color(0xFF800000), CircleShape)
                .clickable { onClick() }
        )
        Text(label, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun SortOption(name: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF800000))
        )
        Text(text = name, style = MaterialTheme.typography.bodyMedium)
    }
}