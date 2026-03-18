<img width="372" height="779" alt="image" src="https://github.com/user-attachments/assets/cdde9b97-5025-4a9f-b47f-a9aeca5af577" />Tugas 4 Pengembangan Aplikasi Mobile

Nama: Fanisa Aulia Safitri
NIM: 123140121

Profile App - State Management & MVVM (Pertemuan 4)

Proyek ini adalah pengembangan aplikasi profil mahasiswa dengan menerapkan pola arsitektur MVVM (Model-View-ViewModel) dan Reactive UI menggunakan Jetpack Compose Multiplatform.

1. Fitur Utama
   Aplikasi ini mencakup, beberapa fitur yaitu:
   - Implementasi MVVM Pattern: Memisahkan UI (View) dari logika bisnis (ViewModel) dan data (Model).
   - UI State Pattern: Menggunakan data class ProfileUiState yang bersifat immutable untuk mengelola seluruh data profil dalam satu sumber kebenaran.
   - State Hoisting: Implementasi komponen LabeledTextField yang bersifat stateless sehingga sangat reusable.
   - Fitur Edit Profile: Form interaktif untuk mengubah Nama dan Bio secara real-time yang terhubung ke ViewModel.
   - Dark Mode Toggle: Fitur perpindahan tema (Gelap/Terang) yang statusnya disimpan dan dikelola di dalam ViewModel.

2. Struktur Proyek
   Folder disusun agar clean code dan memudahkan penilaian:
   - commonMain/kotlin/data/: Tempat file ProfileUiState.kt (Struktur data UI).
   -  commonMain/kotlin/viewmodel/: Tempat file ProfileViewModel.kt (Pengelola logika dan state).
   -  commonMain/kotlin/ui/: Tempat file Composable (ProfileScreen.kt, LabeledTextField.kt).
   -  commonMain/kotlin/App.kt: Entry point utama aplikasi.

3. Detail Teknis
   Berikut merupakan detail teknisnya:
   - StateFlow: Menggunakan MutableStateFlow untuk mengalirkan data secara reaktif.
   - ViewModel Lifecycle: Memastikan data tidak hilang saat terjadi configuration changes (seperti rotasi layar).
   - Compose Recomposition: UI otomatis diperbarui hanya pada bagian yang terdampak saat state di ViewModel berubah.

4. Dokumentasi
   - Profile View (Mode Terang): Tampilan awal profil.
     <img width="366" height="774" alt="image" src="https://github.com/user-attachments/assets/ec9a2389-67af-4209-8110-288cdfa6baea" />
     - Edit Feature: Proses mengubah data melalui TextField.
       <img width="373" height="783" alt="image" src="https://github.com/user-attachments/assets/cebc2bcb-3458-4d64-a41e-6364c23fc17a" />
     - Dark Mode: Tampilan aplikasi saat tema gelap aktif.
       <img width="373" height="783" alt="image" src="https://github.com/user-attachments/assets/a16afa65-d9bf-4b1b-9492-f0df084edd50" />
