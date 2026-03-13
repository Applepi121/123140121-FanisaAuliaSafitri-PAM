**My Profile App - Compose Multiplatform**
Repositori ini berisi proyek tugas praktikum Pertemuan 3 untuk mata kuliah Pengembangan Aplikasi Mobile di Program Studi Teknik Informatika, Institut Teknologi Sumatera . Aplikasi ini adalah aplikasi profil pribadi yang dibangun menggunakan framework Compose Multiplatform (Kotlin Multiplatform) yang dapat dijalankan di Android dan Desktop.
**Informasi Mahasiswa**
- Nama: Fanisa Aulia Safitri
- NIM: 123140121
- Program Studi: Teknik Informatika 
**Capaian Pembelajaran**
Berdasarkan materi pertemuan ini, aplikasi ini telah memenuhi indikator capaian berikut :
- CPMK0501: Mampu membangun UI multiplatform menggunakan Compose.
- CPMK0502: Memahami paradigma UI deklaratif pada Compose Multiplatform.
- Implementasi: Mahasiswa mampu menggunakan Column, Row, Box, Modifier, serta komponen UI dasar seperti Text, Button, Image, dan Card.
**Implementasi Teknis1**
1. Paradigma UI Deklaratif
Aplikasi ini dibangun dengan pendekatan deklaratif, di mana UI dideskripsikan berdasarkan state dan apa yang ingin ditampilkan, bukan instruksi langkah-demi-langkah (imperatif) .
2. Komponen Reusable (@Composable)
Proyek ini memisahkan UI menjadi beberapa fungsi @Composable agar kode bersih dan mudah digunakan kembali (Reusable):
- ProfileHeader: Mengatur tata letak foto profil, nama, dan bio.
- InfoItem: Mengatur baris informasi yang menggabungkan ikon dan teks.
- App: Fungsi utama yang menggabungkan seluruh komponen ke dalam satu kontainer.
  3. Layout Utama
  - Column: Digunakan untuk menyusun elemen profil secara vertikal dari atas ke bawah.
  - Row: Digunakan di dalam InfoItem untuk menyusun ikon dan teks secara horizontal.
  - Box: Digunakan untuk membungkus elemen gambar agar mendapatkan styling yang tepat.
4. Styling dan Modifiers
Penggunaan Modifier diterapkan secara berantai (chaining) dengan memperhatikan urutan eksekusi :
- .fillMaxWidth(): Memastikan komponen memenuhi lebar layar.
- .clip(CircleShape): Membentuk foto profil menjadi lingkaran sempurna.
- .padding(): Mengatur jarak antar elemen agar UI terlihat rapi.
- .border(): Memberikan garis tepi pada foto profil.
**Struktur Proyek**
  Untuk menjalankan aplikasi ini tanpa mengunggah file cache yang besar, berikut adalah file-file krusial yang diunggah :
  - composeApp/src/commonMain/kotlin/: Berisi logika UI utama (App.kt).
  - composeApp/src/commonMain/composeResources/: Berisi aset gambar dan ikon.
  - build.gradle.kts & libs.versions.toml: Konfigurasi dependensi proyek.
**Cara Menjalankan**
1. Clone repositori ini.
2. Buka di Android Studio (Koala atau versi terbaru).
3. Lakukan Gradle Sync dan tunggu hingga selesai.Pilih modul composeApp dan target perangkat (Android Emulator atau Desktop).
4. Klik Run atau tekan Shift + F10.
**Catatan Tambahan**
Aplikasi ini menggunakan tema Material 3 untuk tampilan yang modern dan responsif di berbagai ukuran layar.
"Design is not just what it looks like and feels like. Design is how it works." — Steve Jobs

Dokumentasi Program

