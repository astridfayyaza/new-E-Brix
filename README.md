# 🍃 E-Brix Android Application

**E-Brix** adalah aplikasi Android berbasis **Jetpack Compose (Material 3)** yang dikembangkan untuk memudahkan pencatatan, pemantauan kadar kemanisan gula tebu (**Brix**), serta pengelolaan varietas tebu secara *real-time* dan terstruktur.

Aplikasi ini dibangun menggunakan arsitektur **MVVM (Model-View-ViewModel)** yang bersih, responsif untuk berbagai ukuran layar (Smartphone & Tablet), didukung otentikasi **Google Sign-In**, serta dilengkapi sistem **caching persisten lokal** (`SharedPreferences`) dan integrasi **REST API Client** (`Retrofit2`).

---

## 📚 Folder Dokumentasi Android (`docs/`)

Dokumentasi detail mengenai pengembangan aplikasi Android ini disusun secara terpisah dalam folder `docs/`:
1. [Bab 1: Pengenalan & Latar Belakang Aplikasi](docs/01-Overview.md)
2. [Bab 2: Arsitektur MVVM & Alur Data Android](docs/02-Arsitektur-dan-Alur-Sistem.md)
3. [Bab 3: Fitur & Komponen UI Android (Material 3)](docs/03-Fitur-dan-Komponen-Android.md)
4. [Bab 4: Model Data Domain & Integrasi REST API Client](docs/04-Database-dan-Backend-API.md)
5. [Bab 5: Sistem Autentikasi Google & Manajemen Sesi](docs/05-Sistem-Autentikasi-Google.md)
6. [Bab 6: Panduan Build & Pengujian Aplikasi](docs/06-Panduan-Instalasi-dan-Pengujian.md)
7. [Bab 7: Log Pengubahan & Catatan Pengembangan](docs/07-Catatan-Pengembangan-Selanjutnya.md)

---

## 📐 Arsitektur Aplikasi Android (MVVM)

```text
[ View Layer (Jetpack Compose UI) ]
            │
            ▼  (Observe State via StateFlow)
[ ViewModel Layer (ScanViewModel & AuthViewModel) ]
            │
            ├──► [ Local Cache (SharedPreferences) ]
            │
            ▼  (HTTP / REST API Client via Retrofit2 & Gson)
[ Network Layer (ApiService & ScanDto) ]
```

---

## ✨ Fitur Utama Aplikasi

- 🌾 **Pencatatan Varietas Tebu**: Pemilihan varietas tebu (Bululawang, PS 862, PS 881, PSJK 922, Kidang Kencana, VMC 76-16, M 442-51, dll) melalui dropdown Material 3 yang interaktif.
- 📍 **Pencatatan Data & Lokasi GPS**: Menginput informasi petak lahan, nilai kadar Brix, koordinat lokasi (*Latitude* & *Longitude*), serta *Timestamp* pencatatan.
- 📱 **Antarmuka Responsif Material 3 (Material You)**: Tampilan UI adaptif menggunakan `BoxWithConstraints` yang otomatis menyesuaikan tata letak untuk Smartphone & Tablet (Portrait & Landscape).
- 💾 **Manajemen Cache Persisten Lokal**: Data pilihan varietas tebu tersimpan secara persisten di `SharedPreferences` perangkat agar perubahan data dari pengguna tidak hilang saat aplikasi di-refresh.
- 🔄 **Sinkronisasi Data via REST API Client**: Terhubung secara *online* menggunakan Retrofit2 dengan dukungan *Failover Interceptor* jaringan.
- 🔐 **Autentikasi Google Sign-In**: Login aman menggunakan akun Google dengan pengamanan file konfigurasi `google-services.json` pada `.gitignore`.

---

## 🛠️ Teknologi & Library Utama

- **Bahasa Pemrograman**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3) + Dynamic Responsive Layout
- **Arsitektur**: MVVM (Model-View-ViewModel) + StateFlow
- **Networking**: Retrofit2 & Gson Converter
- **Navigasi**: Navigation Compose
- **Autentikasi**: Google Play Services Auth (`play-services-auth`)
- **Penyimpanan Lokal**: SharedPreferences (`ebrix_user_session` & `ebrix_jenis_tebu_cache`)

---

## 📂 Struktur Project Android

```text
com.aryama0073.e_brix/
├── data/              # Model data domain (ScanData, UserData)
├── network/           # Data Transfer Object (ScanDto) & Interface Retrofit (ApiService)
├── viewmodel/         # Logika bisnis & manajemen state (ScanViewModel, AuthViewModel)
├── ui/                # Komponen UI Compose (HomeScreen, FormScreen, DetailScreen, LoginScreen)
├── navigation/        # Pengatur alur navigasi halaman (AppNav)
└── MainActivity.kt    # Activity utama aplikasi
```
