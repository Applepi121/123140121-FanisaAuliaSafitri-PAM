Tugas Praktikum Kotlin Multiplatform (KMP)
Proyek ini dibuat untuk memenuhi tugas praktikum mengenai pengenalan dan implementasi Kotlin Multiplatform (KMP) dengan menggunakan Compose Multiplatform.

Rincian Tugas
Sesuai dengan instruksi tugas:

Setup & Install: Melakukan instalasi plugin Kotlin Multiplatform di Android Studio.

Project Creation: Membuat proyek baru dengan template Kotlin Multiplatform App.

Modification: Mengubah tampilan standar "Hello World" menjadi identitas diri.

Execution: Menjalankan aplikasi di platform Android/Desktop.

Submission: Mengunggah hasil pekerjaan ke repository GitHub.

Identitas Mahasiswa
Nama: Fanisa

NIM: 123140121

Implementasi Kode
Modifikasi utama dilakukan pada file App.kt di modul commonMain. Berikut adalah cuplikan logika yang diimplementasikan:

Teks Salam: Menampilkan "Halo, Fanisa!" sebagai judul utama.

Identitas: Menampilkan NIM "123140121".

Platform Detection: Menggunakan fungsi getPlatformName() untuk menampilkan sistem operasi yang sedang menjalankan aplikasi secara dinamis.

Cara Menjalankan Aplikasi
1. Platform Android
Pastikan emulator Android atau perangkat fisik sudah terhubung.

Pilih konfigurasi run composeApp pada toolbar Android Studio.

Klik tombol Run (Segitiga Hijau).

2. Platform Desktop
Buka menu Gradle di sisi kanan Android Studio.

Pilih composeApp > Tasks > compose desktop > run.

Atau jalankan perintah ./gradlew :composeApp:run melalui terminal.

Output Aplikasi
Aplikasi akan menampilkan:

Teks: Halo, Fanisa!

Teks: NIM: 123140121

Teks: Platform: Android / Desktop (tergantung platform yang dijalankan).

Dokumentasi:
Ketika belum menekan tombol klik
<img width="334" height="768" alt="Screenshot 2026-02-10 202125" src="https://github.com/user-attachments/assets/d2cdaaab-662f-4f42-ba71-1ed24219b50f" />
Setelah menekan tombol klik
<img width="337" height="735" alt="Screenshot 2026-02-10 202155" src="https://github.com/user-attachments/assets/430ca64d-f859-46f5-ba17-c1ddf114f57d" />

