package com.fanisa.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.*

@Composable
fun App() {
    val manager = remember { NewsFeedManager() }
    var selectedCategory by remember { mutableStateOf("Tech") }

    // State untuk menampung LIST berita yang masuk (banyak berita)
    val newsList = remember { mutableStateListOf<News>() }

    // Mengumpulkan berita dari Flow dan menambahkannya ke list
    LaunchedEffect(selectedCategory) {
        newsList.clear() // Bersihkan list jika ganti kategori
        manager.getNewsFeed()
            .filter { it.category == selectedCategory } // Poin 2: Filter kategori
            .collect { news ->
                newsList.add(0, news) // Tambah berita baru di posisi paling atas
            }
    }

    val totalRead by manager.readCount.collectAsState()

    MaterialTheme {
        Scaffold(
            topBar = {
                Column(Modifier.padding(16.dp)) {
                    Text("News Feed Simulator", style = MaterialTheme.typography.headlineMedium)
                    Text("Total Dibaca: $totalRead", style = MaterialTheme.typography.bodySmall)

                    // Pilihan Filter Kategori
                    Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Tech", "Campus", "Design").forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            // Menampilkan banyak berita ke bawah (Scrollable) [cite: 588]
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(newsList) { news ->
                    NewsCard(news, onRead = { manager.markAsRead() })
                }
            }
        }
    }
}

@Composable
fun NewsCard(news: News, onRead: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Poin 3: Transformasi tampilan [cite: 588]
            Text("HOT NEWS [${news.category}]", color = MaterialTheme.colorScheme.primary)
            Text(text = news.title, style = MaterialTheme.typography.titleMedium)
            Text(text = news.content, style = MaterialTheme.typography.bodySmall)

            Button(
                onClick = onRead,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Tandai Dibaca")
            }
        }
    }
}