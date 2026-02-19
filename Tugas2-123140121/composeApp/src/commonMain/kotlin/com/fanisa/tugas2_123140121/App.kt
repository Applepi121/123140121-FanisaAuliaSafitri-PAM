package com.fanisa.tugas2_123140121

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// =====================================================
// DATA LAYER
// =====================================================

enum class TopikBerita(val label: String) {
    SEMUA("Semua"),
    RISET("Riset & IT"),
    PENGABDIAN("Sosial & UMKM")
}

data class ArtikelBerita(
    val kode: Int,
    val judul: String,
    val topik: TopikBerita
)

// =====================================================
// REPOSITORY (KONTEN RELEVAN DENGAN FANISA)
// =====================================================

class ArtikelRepository {
    // Daftar artikel berdasarkan aktivitas Fanisa (Stunting, UMKM, UI/UX)
    private val daftarArtikel = listOf(
        ArtikelBerita(1, "Sistem Pakar Monitoring Stunting", TopikBerita.RISET),
        ArtikelBerita(2, "Redesain Web Pemprov Lampung", TopikBerita.RISET),
        ArtikelBerita(3, "Digital Branding UMKM Way Galih", TopikBerita.PENGABDIAN),
        ArtikelBerita(4, "Implementasi Forward Chaining", TopikBerita.RISET),
        ArtikelBerita(5, "Pelatihan Desain Kemasan Produk", TopikBerita.PENGABDIAN)
    )

    fun semuaArtikel(): List<ArtikelBerita> = daftarArtikel

    // FITUR 1: Cold Flow untuk Streaming Berita
    fun streamArtikel(): Flow<ArtikelBerita> = flow {
        var indeks = 0
        while (true) {
            val artikel = daftarArtikel[indeks % daftarArtikel.size]
            emit(artikel)
            indeks++
            delay(2_000L)
        }
    }.catch {
        emit(ArtikelBerita(0, "Gagal sinkronisasi data", TopikBerita.SEMUA))
    }

    // FITUR 5: Coroutine async/await
    suspend fun muatDetailArtikel(kode: Int): String {
        return withContext(Dispatchers.Default) {
            val hasilAsync = async {
                delay(800L)
                "Data riset artikel #$kode berhasil dimuat."
            }
            hasilAsync.await()
        }
    }
}

// =====================================================
// VIEWMODEL
// =====================================================

class BeritaViewModel {
    val repo = ArtikelRepository()

    // FITUR 4: StateFlow untuk Counter
    private val _jumlahDibuka = MutableStateFlow(0)
    val jumlahDibuka: StateFlow<Int> = _jumlahDibuka.asStateFlow()

    val topikAktif = MutableStateFlow(TopikBerita.SEMUA)

    // FITUR 2 & 3: Filter & Transformasi Flow
    @OptIn(ExperimentalCoroutinesApi::class)
    val judulTeratas: Flow<String> = topikAktif.flatMapLatest { topik ->
        repo.streamArtikel()
            .filter { artikel ->
                topik == TopikBerita.SEMUA || artikel.topik == topik
            }
            .map { it.judul.uppercase() }
    }

    val semuaArtikel: List<ArtikelBerita> = repo.semuaArtikel()

    fun tandaiBaca() { _jumlahDibuka.value++ }
    fun gantiTopik(topik: TopikBerita) { topikAktif.value = topik }
}

// =====================================================
// UI — COMPOSABLE (YELLOW THEME)
// =====================================================

private val WarnaUtama = Color(0xFFFFD700) // Kuning Emas
private val WarnaAksen = Color(0xFFFFC107) // Amber
private val WarnaKontras = Color(0xFF212121) // Teks Gelap
private val WarnaBackground = Color(0xFFFFFDE7) // Kuning Pucat

@Composable
fun App() {
    val viewModel = remember { BeritaViewModel() }
    val coroutineScope = rememberCoroutineScope()

    val judulAktif by viewModel.judulTeratas.collectAsState(initial = "MENYINKRONKAN...")
    val totalDibuka by viewModel.jumlahDibuka.collectAsState()
    val topikDipilih by viewModel.topikAktif.collectAsState()

    MaterialTheme {
        Scaffold(
            bottomBar = {
                BottomAppBar(containerColor = WarnaUtama) {
                    Text(
                        text = "Total Artikel Diakses: $totalDibuka",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = WarnaKontras,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarnaBackground)
                    .padding(innerPadding)
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // HEADER SECTION
                item {
                    Column {
                        Text("Fanisa's Journal", fontSize = 32.sp, fontWeight = FontWeight.Black, color = WarnaKontras)
                        Text("Informatics Engineering | 123140121", color = Color.Gray, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(20.dp))
                }

                // FILTER SECTION
                item {
                    Text("KATEGORI RISET:", style = MaterialTheme.typography.labelLarge, color = Color.Black)
                    Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TopikBerita.entries.forEach { topik ->
                            val isSelected = topik == topikDipilih
                            Button(
                                onClick = { viewModel.gantiTopik(topik) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) WarnaAksen else Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                elevation = ButtonDefaults.buttonElevation(2.dp)
                            ) {
                                Text(topik.label, color = if (isSelected) Color.Black else Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // LIVE HIGHLIGHT SECTION
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = WarnaUtama),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(Modifier.padding(24.dp)) {
                            Text("● LIVE UPDATE", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(Modifier.height(8.dp))
                            AnimatedContent(
                                targetState = judulAktif,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = ""
                            ) { teks ->
                                Text(teks, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = WarnaKontras)
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // ARTICLE LIST
                item {
                    Text("DAFTAR ARTIKEL TERBARU", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                }

                items(viewModel.semuaArtikel) { artikel ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(40.dp).background(WarnaUtama.copy(0.2f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                                Text("#${artikel.kode}", fontWeight = FontWeight.Bold, color = Color(0xFF795548))
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text(artikel.judul, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(artikel.topik.label, fontSize = 12.sp, color = WarnaAksen)
                            }
                            IconButton(onClick = {
                                viewModel.tandaiBaca()
                                coroutineScope.launch { viewModel.repo.muatDetailArtikel(artikel.kode) }
                            }) {
                                Text("Detail", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
                            }
                        }
                    }
                }
            }
        }
    }
}