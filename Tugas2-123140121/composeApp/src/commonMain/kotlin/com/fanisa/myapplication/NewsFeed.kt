package com.fanisa.myapplication

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class News(val id: Int, val title: String, val category: String, val content: String)

class NewsFeedManager {
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    // Poin 1: Flow mensimulasikan data baru setiap 2 detik
    fun getNewsFeed(): Flow<News> = flow {
        val newsList = listOf(
            News(1, "Kotlin Multiplatform Terbaru", "Tech", "KMP kini mendukung lebih banyak library."),
            News(2, "Update KKN ITERA 2026", "Campus", "Pendaftaran KKN periode genap telah dibuka."),
            News(3, "Optimasi Coroutines di PAAM", "Tech", "Gunakan Dispatchers.IO untuk efisiensi."),
            News(4, "Tips UI/UX Mobile Design", "Design", "Warna maroon memberikan kesan elegan."),
            News(5, "Fitur Baru Kotlin Flow", "Tech", "StateFlow mempermudah state management."),
            News(6, "Webinar HMIF Mendatang", "Campus", "Jangan lewatkan sesi berbagi alumni."),
            News(7, "Machine Learning di Android", "Tech", "Implementasi model AI kini lebih ringan.")
        )
        // Simulasi data terus bertambah ke bawah
        for (news in newsList) {
            delay(2000) // Delay 2 detik
            emit(news)
        }
    }.flowOn(Dispatchers.Default)

    // Poin 5: Coroutines untuk mengambil detail secara async
    suspend fun fetchNewsDetail(id: Int): String = withContext(Dispatchers.IO) {
        delay(500)
        "Konten lengkap untuk artikel #$id telah dimuat."
    }

    fun markAsRead() {
        _readCount.value++
    }
}