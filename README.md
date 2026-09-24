# 🍃 E-Brix Mobile Application & Backend System

**E-Brix** adalah aplikasi Android berbasis Jetpack Compose yang dirancang untuk membantu pemindaian, ekstraksi nilai Brix (kadar gula/kemanisan) secara otomatis dari alat ukur (refraktometer) menggunakan **ML Kit Text Recognition (OCR)**, serta menyimpan data hasil pemindaian secara *real-time* ke database **PostgreSQL** melalui **REST API Backend**.

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
[ Backend Node.js / Express.js ]
            │
            ▼  (Driver pg - PostgreSQL Client)
[ Database PostgreSQL ]
```

---

## ✨ Fitur Utama

- 📷 **Pengambilan Foto Refraktometer & OCR**: Mengambil foto angka Brix dan mengekstraksinya secara otomatis menggunakan **Google ML Kit Text Recognition**.
- 📍 **Pencatatan Metadata**: Menyimpan informasi lokasi (*Latitude* & *Longitude*), petak lahan, dan *Timestamp* secara otomatis.
- 🔄 **Sinkronisasi Database**: Terhubung secara *real-time* dengan database PostgreSQL untuk membaca dan menyimpan data scan.
- 📱 **Antarmuka Modern**: Dibangun menggunakan **Jetpack Compose** & **Material 3** yang reponsif.
- 🔌 **Dukungan HP Fisik & Emulator**: Mendukung pengujian menggunakan HP fisik via USB port-forwarding (`adb reverse`) maupun Android Emulator.

---

## 🛠️ Teknologi & Library Utama

### **Android Application (Client)**
- **Bahasa**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Arsitektur**: MVVM (Model-View-ViewModel) + StateFlow
- **Networking**: Retrofit2 & Gson Converter
- **Machine Learning**: Google ML Kit Text Recognition
- **Kamera**: CameraX / MediaStore Camera Intent
- **Navigasi**: Navigation Compose

### **Backend & Database**
- **Runtime**: Node.js
- **Framework Backend**: Express.js
- **Database**: PostgreSQL (v14+)
- **Database Client**: `pg` (node-postgres)
- **CORS Middleware**: `cors`

---
2. **Cleartext Traffic**: Fitur `android:usesCleartextTraffic="true"` diaktifkan pada `AndroidManifest.xml` untuk mendukung komunikasi HTTP lokal selama proses *development*.
3. **Optimasi Payload Gambar**: Kompresi gambar disesuaikan pada resolusi JPEG 70% dan dikirim dalam format **Base64** tanpa membebankan *memory/heap* perangkat.
