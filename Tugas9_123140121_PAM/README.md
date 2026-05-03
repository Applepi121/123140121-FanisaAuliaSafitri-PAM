# UpgradeNote — Tugas 9: Integrasi AI API

**Nama:** Fanisa Aulia Safitri
**NIM:** 123140121
**Kelas:** RB

## Deskripsi

UpgradeNote adalah aplikasi manajemen catatan berbasis **Kotlin Multiplatform (KMP)** yang pada Tugas 9 ini ditambahkan fitur **Analisis Nutrisi AI** menggunakan integrasi Large Language Model (LLM). Fitur ini memungkinkan pengguna menganalisis kandungan gizi makanan hanya dengan mengetikkan nama makanan dalam bahasa apapun, atau dengan mengambil foto makanan menggunakan kamera perangkat.

## Fitur Baru — Analisis Nutrisi AI

### 1. Analisis dari Teks (Multi-bahasa)
- Input nama makanan dalam bahasa apapun
- Mendukung pilihan porsi dan satuan (gram, cup, sendok makan, dll.)
- Quick picks untuk makanan populer Indonesia
- Hasil mencakup: kalori, protein, karbohidrat, lemak, serat, gula, sodium, kolesterol
- Health Score 1–10 dengan kategori dan penjelasan
- Saran gizi personal dari AI

### 2. Analisis dari Foto Makanan (Kamera)
- Buka kamera langsung dari aplikasi
- Pilih gambar dari galeri
- AI mendeteksi jenis makanan dari foto secara otomatis
- Menggunakan Groq Vision API (Llama 4 Scout)

### 3. Fitur
- Manajemen catatan (CRUD) dengan SQLDelight
- Dependency Injection dengan Koin
- Network Status Monitoring (offline banner)
- Tema warna (Default / Pink / Dark)
- Profile screen
- Settings

## Arsitektur AI Integration

```
User Input (Teks / Foto)
        ↓
  NutritionScreen.kt          ← UI Layer (Compose)
        ↓
  NutritionViewModel.kt       ← ViewModel (State Management)
        ↓
   AIRepository.kt            ← Repository (Retry + Error Handling)
        ↓
   GeminiService.kt           ← Service (HTTP → Groq API)
        ↓
  Groq API (LLaMA 3.3 70B)   ← LLM Processing
        ↓
   NutritionInfo.kt           ← Data Model (JSON Parsed)
```

---

## Struktur Proyek (Penambahan Tugas 9)

```
composeApp/src/
├── commonMain/kotlin/com/fanisa/upgradenote/
│   ├── data/ai/
│   │   ├── ApiConfig.kt          # expect/actual API key
│   │   ├── GeminiModels.kt       # Data models (NutritionInfo, AIError)
│   │   ├── GeminiService.kt      # HTTP client → Groq API
│   │   └── AIRepository.kt       # Retry logic + error mapping
│   ├── presentation/
│   │   ├── ui/NutritionScreen.kt         # UI lengkap (tab teks + kamera)
│   │   └── viewmodel/NutritionViewModel.kt  # State + cooldown timer
│   └── di/AppModule.kt           # Koin module (tambah AI dependencies)
│
├── androidMain/kotlin/com/fanisa/upgradenote/
│   ├── data/ai/ApiConfig.android.kt  # actual BuildConfig
│   ├── CameraHelper.kt               # Uri → Base64 converter
│   └── MainActivity.kt               # Camera/Gallery launcher
│
└── androidMain/
    ├── AndroidManifest.xml       # INTERNET + CAMERA permission
    └── res/xml/file_paths.xml    # FileProvider paths
```

## Prompt Engineering

Prompt dirancang dengan pola **Role + Task + Format + Constraint**:

[ROLE]   "You are a professional nutritionist with 10 years of experience."
[TASK]   "Analisis kandungan gizi: {foodName} ({amount} {unit})"
[FORMAT] Struktur JSON ketat dengan semua field wajib
[CONSTRAINT] "Respond ONLY with valid JSON. No markdown, no backticks."

Mendukung deteksi makanan dari semua bahasa dan selalu mengembalikan hasil dalam Bahasa Indonesia.

## Setup & Konfigurasi

### Prasyarat
- Android Studio Hedgehog atau lebih baru
- JDK 11
- Android SDK 24+
- Groq API Key (gratis di [console.groq.com](https://console.groq.com))

### Langkah Setup

1. Clone repository:
2. Tambahkan API key di local.properties
3. Sync Gradle dan Run

Aplikasi ini menggunakan **Groq** karena limit gratis yang lebih besar dan kecepatan respons yang lebih tinggi, cocok untuk development dan demo.

**Dokumentasi:**
Tampilan Pencarian makanan:
<img width="720" height="1600" alt="WhatsApp Image 2026-05-04 at 01 14 50" src="https://github.com/user-attachments/assets/5d7ea952-e583-40ba-a41c-0c73b657f91b" />


Tampilan hasil dari pencarian makanan:
<img width="720" height="1600" alt="WhatsApp Image 2026-05-04 at 01 14 49" src="https://github.com/user-attachments/assets/a9e49ce7-a822-4724-9fb9-49b1f1c3c7d8" />

<img width="720" height="1600" alt="WhatsApp Image 2026-05-04 at 01 14 49 (1)" src="https://github.com/user-attachments/assets/c360b3d7-a090-4869-ab2d-5aca7158ddc4" />


Penilaian makanan dari kamera:
<img width="720" height="1600" alt="WhatsApp Image 2026-05-04 at 01 14 48" src="https://github.com/user-attachments/assets/764c6d72-631e-4824-9aeb-9efd39fae3bf" />


Makanan yang difoto:
<img width="720" height="1600" alt="WhatsApp Image 2026-05-04 at 01 14 48 (1)" src="https://github.com/user-attachments/assets/73c6f034-d86f-4972-8849-8bc6aa369c1c" />


Hasil analisis makanan dari kamera:
<img width="720" height="1600" alt="WhatsApp Image 2026-05-04 at 01 14 48 (2)" src="https://github.com/user-attachments/assets/32ad9539-d41e-438c-85a3-a0872b08ad5e" />
