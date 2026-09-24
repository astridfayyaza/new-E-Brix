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

## 📁 Struktur Project

```text
E-Brix-Android/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/aryama0073/e_brix/
│           │   ├── data/
│           │   │   └── ScanData.kt              # Data Domain Model
│           │   ├── network/
│           │   │   ├── ApiService.kt            # Retrofit Interface & Client Config
│           │   │   └── ScanDto.kt               # Data Transfer Object & Mappers
│           │   ├── navigation/
│           │   │   └── AppNav.kt                # Jetpack Compose Navigation Host
│           │   ├── ocr/
│           │   │   └── OCRProcessor.kt          # ML Kit Text Recognition Logic
│           │   ├── ui/
│           │   │   ├── HomeScreen.kt            # Halaman Utama (Daftar Scan)
│           │   │   ├── FormScreen.kt            # Halaman Form Tambah Scan
│           │   │   ├── DetailScreen.kt          # Halaman Detail Scan
│           │   │   └── theme/                   # Material 3 Color & Theme Config
│           │   ├── viewmodel/
│           │   │   └── ScanViewModel.kt         # ViewModel Mengelola State & API Call
│           │   └── MainActivity.kt              # Entry Point Aplikasi
│           └── AndroidManifest.xml              # Izin Kamera, Internet & Cleartext Config
├── build.gradle.kts                             # Root Gradle Config
└── README.md                                    # Dokumentasi System
```

---

## 🗄️ 1. Panduan Setup Database PostgreSQL

### 1.1 Buat Tabel Database
Jalankan query SQL berikut di **pgAdmin**, **DBeaver**, atau **psql**:

```sql
-- 1. Buat database (jika belum ada)
CREATE DATABASE ebrix_db;

-- 2. Buat tabel 'scans'
CREATE TABLE IF NOT EXISTS scans (
    id SERIAL PRIMARY KEY,
    petak VARCHAR(100) NOT NULL,
    image_base64 TEXT,
    brix VARCHAR(50) NOT NULL,
    lat VARCHAR(50),
    lon VARCHAR(50),
    timestamp VARCHAR(100)
);

-- 3. Masukkan data awal untuk uji coba (opsional)
INSERT INTO scans (petak, brix, lat, lon, timestamp) 
VALUES ('Petak A1 Contoh', '18.5', '-6.200000', '106.816666', '28/02/2025 10:00:00');
```

---

## 🚀 2. Panduan Setup Backend Node.js

### 2.1 Inisialisasi Backend
Buat folder terpisah di luar project Android (contoh: `ebrix-backend`):

```bash
mkdir ebrix-backend
cd ebrix-backend
npm init -y
npm install express pg cors
```

### 2.2 Kodingan `server.js`
Buat file `server.js` di dalam folder `ebrix-backend`:

```javascript
const express = require('express');
const { Pool } = require('pg');
const cors = require('cors');

const app = express();

// Middleware
app.use(cors());
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ limit: '50mb', extended: true }));

// 🔹 Konfigurasi PostgreSQL
const pool = new Pool({
    user: 'postgres',
    host: 'localhost',
    database: 'postgres', // Sesuaikan nama database Anda
    password: 'admin123', // Sesuaikan password PostgreSQL Anda
    port: 5432,
});

// Test Koneksi PostgreSQL
pool.connect((err, client, release) => {
    if (err) {
        console.error('❌ GAGAL terhubung ke database PostgreSQL:', err.message);
    } else {
        console.log('✅ BERHASIL terhubung ke database PostgreSQL');
        release();
    }
});

// 🔹 REST API Endpoints

// Root Test
app.get('/', (req, res) => {
    res.send('Server E-Brix Backend Berjalan!');
});

// GET /api/scans - Ambil semua data scan
app.get('/api/scans', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM scans ORDER BY id DESC');
        res.status(200).json(result.rows);
    } catch (err) {
        console.error('❌ Error GET /api/scans:', err.message);
        res.status(500).json({ error: 'Gagal mengambil data', details: err.message });
    }
});

// POST /api/scans - Simpan data scan baru
app.post('/api/scans', async (req, res) => {
    try {
        const { petak, image_base64, brix, lat, lon, timestamp } = req.body;
        const queryText = `
            INSERT INTO scans (petak, image_base64, brix, lat, lon, timestamp)
            VALUES ($1, $2, $3, $4, $5, $6)
            RETURNING *
        `;
        const values = [petak, image_base64 || null, brix, lat, lon, timestamp];
        const result = await pool.query(queryText, values);

        console.log('✅ Data berhasil disimpan dengan ID:', result.rows[0].id);
        res.status(201).json(result.rows[0]);
    } catch (err) {
        console.error('❌ Error POST /api/scans:', err.message);
        res.status(500).json({ error: 'Gagal menyimpan data', details: err.message });
    }
});

// Jalankan Server
const PORT = 3000;
app.listen(PORT, '0.0.0.0', () => {
    console.log(`🚀 Server Backend E-Brix berjalan di http://localhost:${PORT}`);
});
```

### 2.3 Jalankan Server Backend
```bash
node server.js
```

---

## 📱 3. Panduan Setup Aplikasi Android (E-Brix)

### 3.1 Konfigurasi IP pada `ApiService.kt`
Buka file `app/src/main/java/com/aryama0073/e_brix/network/ApiService.kt`:

- **Skenario A: HP Asli via Kabel USB (ADB Reverse)**:
  ```kotlin
  const val BASE_URL = "http://127.0.0.1:3000/"
  ```
- **Skenario B: Android Emulator bawaan Android Studio**:
  ```kotlin
  const val BASE_URL = "http://10.0.2.2:3000/"
  ```
- **Skenario C: HP Asli via Wi-Fi**:
  ```kotlin
  const val BASE_URL = "http://192.168.x.x:3000/" // Alamat IP Wi-Fi laptop Anda
  ```

### 3.2 Port Forwarding USB (Khusus HP Fisik USB)
Jika Anda menggunakan HP fisik terhubung via USB, jalankan perintah berikut di PowerShell / Terminal komputer:

```powershell
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -d reverse tcp:3000 tcp:3000
```

### 3.3 Jalankan Aplikasi
1. Buka project **E-Brix** di Android Studio.
2. Hubungkan HP atau Emulator.
3. Klik tombol **Run ▶️** (Shift + F10).

---

## 🌐 4. Spesifikasi API Endpoints

| Method | Endpoint | Deskripsi | Body Request (JSON) | Status Response |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/scans` | Mengambil seluruh riwayat hasil scan | *None* | `200 OK` (Array JSON) |
| `POST` | `/api/scans` | Menyimpan data scan baru ke PostgreSQL | `{ "petak": "String", "image_base64": "String?", "brix": "String", "lat": "String", "lon": "String", "timestamp": "String" }` | `201 Created` |

---

## 🛡️ Catatan Keamanan & Best Practice
1. **Pemisahan Akses Database**: Aplikasi Android tidak terhubung langsung ke PostgreSQL secara native/JDBC demi menjaga kerahasiaan credential database.
2. **Cleartext Traffic**: Fitur `android:usesCleartextTraffic="true"` diaktifkan pada `AndroidManifest.xml` untuk mendukung komunikasi HTTP lokal selama proses *development*.
3. **Optimasi Payload Gambar**: Kompresi gambar disesuaikan pada resolusi JPEG 70% dan dikirim dalam format **Base64** tanpa membebankan *memory/heap* perangkat.
