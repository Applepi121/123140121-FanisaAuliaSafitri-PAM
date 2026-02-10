package com.fanisa.tugaskmpfanisa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    // Definisi Warna Maroon dan Hitam
    val maroon = Color(0xFF800000)
    val charcoalBlack = Color(0xFF121212) // Hitam matte agar lebih nyaman di mata
    val softGrey = Color(0xFFF5F5F5)      // Untuk latar belakang terang atau teks sekunder

    val maroonColorScheme = lightColorScheme(
        primary = maroon,
        onPrimary = Color.White,
        surface = Color.White,       // Latar belakang utama
        onSurface = charcoalBlack    // Warna teks pada latar belakang
    )

    MaterialTheme(colorScheme = maroonColorScheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
            var isVisible by remember { mutableStateOf(false) }

            val platformName = remember { getPlatform().name }

            val platformInfo = if (platformName.contains("Java", ignoreCase = true)) {
                "Desktop 💻"
            } else {
                "Andro 📱"
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Halo, Fanisa Aulia Safitri!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = maroon // Menggunakan Maroon
                )

                Text(
                    text = "NIM: 123140121",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = charcoalBlack // Menggunakan Hitam
                )

                Text(
                    text = "Running on: $platformInfo",
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Button(
                    onClick = { isVisible = !isVisible },
                    // Tombol berwarna Maroon
                    colors = ButtonDefaults.buttonColors(containerColor = maroon)
                ) {
                    Text(if (isVisible) "Sembunyikan" else "Klik!", color = Color.White)
                }

                AnimatedVisibility(isVisible) {
                    Text(
                        text = "Tugas Praktikum 1 Berhasil! 🎉",
                        modifier = Modifier.padding(top = 16.dp),
                        color = maroon,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}