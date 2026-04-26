**UpgradeNote - Kotlin Multiplatform (Tugas Minggu 8)**

Nama: Fanisa Aulia Safitri
NIM: 123140121
Kelas: RB

pgradeNote adalah aplikasi manajemen catatan berbasis Kotlin Multiplatform (KMP) yang dirancang untuk mendemonstrasikan implementasi fitur spesifik platform, manajemen dependensi, dan arsitektur aplikasi yang bersih.

**Fitur Utama**
Aplikasi ini mengimplementasikan:
1. Koin Dependency Injection (DI)
2. Device Information (Expect/Actual)
3. Network Status Monitoring

**Tech Stack**
Language: Kotlin
UI Framework: Compose Multiplatform
Dependency Injection: Koin
Local Storage: SQLDelight / Multiplatform Settings
Navigation: Navigation Compose Stack
Architecture: MVVM (Model-View-ViewModel)

**Struktur Proyek**
Plaintext
composeApp/
├── src/
│   ├── commonMain/         # Logika bisnis, UI Utama, dan Expect DI
│   │   └── kotlin/di/      # AppModule.kt (Expect platformModule)
│   ├── androidMain/        # Implementasi native Android dan Actual DI
│   │   ├── kotlin/di/      # PlatformModule.android.kt
│   │   └── AndroidManifest # Izin ACCESS_NETWORK_STATE
│   └── iosMain/            # Implementasi native iOS

**Identitas Visual**
Aplikasi ini menggunakan tema warna Maroon dan Black sebagai identitas utama proyek UpgradeNote, memberikan tampilan yang elegan dan tegas sesuai preferensi desain pengembang.

Link Video Demo: https://youtu.be/PReIaDReT-I?si=NMzr9kuJKVXzTdMv

Dokumentasi:
Tampilan info aplikasi
<img width="834" height="766" alt="image" src="https://github.com/user-attachments/assets/191ea978-8851-49b1-8bf0-c479a476a197" />

Tampilan jika offline
<img width="850" height="756" alt="image" src="https://github.com/user-attachments/assets/7ceb912b-7c20-44d8-a013-c0fbbcfcf20f" />
