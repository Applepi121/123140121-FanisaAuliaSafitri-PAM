Nama: Fanisa Aulia Safitri
Nim: 123140121

News Feed Simulator - Kotlin Multiplatform
Proyek ini adalah aplikasi simulasi berita yang dibangun menggunakan Kotlin Multiplatform (KMP) untuk memenuhi tugas Pertemuan 2 mata kuliah Pengembangan Aplikasi Mobile. Aplikasi ini mendemonstrasikan penggunaan fitur Advanced Kotlin, Coroutines, dan Kotlin Flow dalam lingkungan asynchronous.

Fitur Utama
Aplikasi ini mengimplementasikan spesifikasi teknis sesuai rubrik penilaian:
1. Flow Data Simulation: Simulasi aliran data berita baru yang muncul secara otomatis setiap 2 detik menggunakan Flow builder.
2. Kategori Filtering: Fitur untuk menyaring berita berdasarkan kategori tertentu (Tech, Campus, Design) menggunakan operator .filter.
3. Data Transformation: Mengubah format data mentah menjadi tampilan "HOT NEWS" menggunakan operator .map.
4. State Management: Melacak jumlah berita yang telah dibaca secara real-time menggunakan StateFlow.
5. Async Detail Fetching: Simulasi pengambilan detail konten berita di background thread menggunakan Coroutines (suspend function & withContext) agar UI tetap responsif.

Teknologi yang Digunakan

- Kotlin Coroutines: Untuk pemrograman asynchronous non-blocking.
- Kotlin Flow: Untuk menangani stream data secara sequential.
- Compose Multiplatform: Framework untuk membangun antarmuka pengguna (UI).
- Structured Concurrency: Memastikan coroutine berjalan dalam scope yang aman untuk menghindari memory leaks.

Struktur Proyek

- commonMain/kotlin/.../NewsFeed.kt: Logika bisnis, Flow producer, dan News Manager.
- commonMain/kotlin/.../App.kt: Implementasi UI menggunakan Compose dan pengumpulan (collect) data Flow.
- androidMain/: Konfigurasi spesifik platform Android

Cara Menjalankan
1. Buka proyek menggunakan Android Studio.
2. Pastikan dependency coroutines sudah terpasang di build.gradle.kts.
3. Pilih Run Configuration ke composeApp.
4. Jalankan pada Emulator Android atau perangkat fisik.
5. Pilih kategori pada Filter Chip untuk melihat simulasi data yang masuk setiap 2 detik.

Screenshoot hasil run 
<img width="383" height="808" alt="image" src="https://github.com/user-attachments/assets/86bc6f0d-6b59-4fb9-ac79-e8bebc863f79" />
