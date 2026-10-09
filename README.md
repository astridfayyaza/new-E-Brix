# 🍃 E-Brix Mobile Application & Backend System

**E-Brix** adalah aplikasi Android berbasis Jetpack Compose yang dirancang untuk membantu pemindaian, ekstraksi nilai Brix (kadar gula/kemanisan) secara otomatis dari alat ukur (refraktometer) menggunakan **ML Kit Text Recognition (OCR)**, pencatatan varietas tebu, serta menyimpan data hasil pemindaian secara *real-time* ke database **PostgreSQL** melalui **REST API Backend**.

---

## 📚 Folder Dokumentasi Lengkap (`docs/`)

Dokumentasi detail disusun secara terpisah dalam bentuk bab-bab Markdown pada folder `docs/`:
1. [Bab 1: Pengenalan & Latar Belakang](docs/01-Overview.md)
2. [Bab 2: Arsitektur & Alur Kerja Sistem](docs/02-Arsitektur-dan-Alur-Sistem.md)
3. [Bab 3: Fitur & Komponen Aplikasi Android](docs/03-Fitur-dan-Komponen-Android.md)
4. [Bab 4: Database PostgreSQL & Backend REST API](docs/04-Database-dan-Backend-API.md)
5. [Bab 5: Sistem Autentikasi Google & Manajemen Sesi](docs/05-Sistem-Autentikasi-Google.md)
6. [Bab 6: Panduan Instalasi & Pengujian System](docs/06-Panduan-Instalasi-dan-Pengujian.md)
7. [Bab 7: Catatan Pengembangan & Rencana Fitur Selanjutnya](docs/07-Catatan-Pengembangan-Selanjutnya.md)

---

## 📐 Arsitektur Sistem

```text
[ Aplikasi Android E-Brix ] 
            │
            ▼  (HTTP / REST API - JSON via Retrofit)
[ Backend Flask (Python) / Node.js ]
            │
            ▼  (Driver Database - PostgreSQL Client)
[ Database PostgreSQL ]
```

---

## ✨ Fitur Utama

- 📷 **Pengambilan Foto Refraktometer & OCR**: Mengambil foto angka Brix dan mengekstraksinya secara otomatis menggunakan **Google ML Kit Text Recognition**.
- 🌾 **Pencatatan Varietas Tebu**: Mendukung pemilihan jenis/varietas tebu (Bululawang, PS 862, PS 881, PSJK 922, Kidang Kencana, VMC 76-16, dll) melalui dropdown Material 3.
- 📍 **Pencatatan Metadata**: Menyimpan informasi lokasi (*Latitude* & *Longitude*), petak lahan, dan *Timestamp* secara otomatis.
- 🔄 **Sinkronisasi Database & Cache Lokal**: Terhubung secara *real-time* dengan database backend dan dilengkapi caching lokal `SharedPreferences` agar data varietas tebu tersimpan aman.
- 📱 **Antarmuka Responsif Material 3 (Material You)**: Dibangun menggunakan **Jetpack Compose** & `BoxWithConstraints` yang responsif di berbagai ukuran layar (Smartphone & Tablet, Portrait & Landscape).
- 🔐 **Autentikasi Akun Google**: Login menggunakan Google Sign-In yang aman dengan perlindungan credential file `google-services.json` di `.gitignore`.

---

## 🛠️ Teknologi & Library Utama

### **Android Application (Client)**
- **Bahasa**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3) + Layout Responsif
- **Arsitektur**: MVVM (Model-View-ViewModel) + StateFlow
- **Networking**: Retrofit2 & Gson Converter
- **Machine Learning**: Google ML Kit Text Recognition
- **Kamera**: CameraX / MediaStore Camera Intent
- **Navigasi**: Navigation Compose

### **Backend & Database**
- **Runtime**: Python (Flask) / Node.js (Express)
- **Database**: PostgreSQL (v14+)
- **Driver**: `psycopg2-binary` (Python) / `pg` (Node.js)
- **CORS Middleware**: `flask-cors` / `cors`

---
