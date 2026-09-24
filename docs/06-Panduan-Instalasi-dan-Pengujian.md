# 🚀 Bab 6: Panduan Instalasi & Pengujian System

## 📋 Prasyarat Perangkat & Perangkat Lunak

Sebelum menjalankan project E-Brix, pastikan perangkat komputer/laptop Anda telah terpasang:
- **Android Studio** (Versi Jellyfish / Ladybug atau yang terbaru).
- **Node.js** (Versi 18 atau yang terbaru).
- **PostgreSQL** (Versi 14 atau yang terbaru).
- **Kabel USB** & HP Android fisik dengan **USB Debugging** diaktifkan (atau Android Emulator).

---

## 🛠️ Langkah-Langkah Running Project

### Langkah 1: Persiapan Database PostgreSQL
1. Buka **pgAdmin** atau **psql**.
2. Buat database dan jalankan query pembuatan tabel `scans` (lihat *Bab 4*).

### Langkah 2: Jalankan Server Backend Node.js
1. Buka Terminal / CMD di folder server backend Anda (`ebrix-backend`).
2. Jalankan perintah instalasi dependensi (jika belum):
   ```bash
   npm install express pg cors
   ```
3. Jalankan server:
   ```bash
   node server.js
   ```
4. Pastikan di terminal muncul pesan:
   ```text
   🚀 Server Backend E-Brix berjalan di http://localhost:3000
   ✅ BERHASIL terhubung ke database PostgreSQL
   ```

### Langkah 3: Aktifkan Port Forwarding USB (Khusus HP Fisik USB)
Buka PowerShell di komputer Anda, lalu jalankan perintah berikut agar port `3000` di HP diteruskan ke laptop Anda:

```powershell
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -d reverse tcp:3000 tcp:3000
```

### Langkah 4: Jalankan Aplikasi di Android Studio
1. Buka folder `E-Brix-Android` di Android Studio.
2. Hubungkan HP fisik Anda lewat kabel USB.
3. Klik tombol **Run ▶️** (hijau) di bagian atas Android Studio.

---

## 🧪 Skenario Pengujian (Testing Steps)

1. **Pengujian Sign-In**:
   - Saat aplikasi pertama kali terbuka di HP, pastikan halaman `LoginScreen` tampil.
   - Klik **"Sign in with Google"**, pilih akun Gmail Anda.
   - Pastikan muncul Toast *"Selamat datang, [Nama]!"* dan aplikasi berpindah ke Beranda.

2. **Pengujian Baca Data dari PostgreSQL**:
   - Pastikan data contoh dari database PostgreSQL otomatis muncul di daftar halaman Beranda.
   - Coba tekan ikon **Refresh** di TopBar untuk memperbarui data.

3. **Pengujian Tambah Data Scan Baru**:
   - Tekan tombol **`+`** (Floating Action Button).
   - Isi nama petak lahan (misal: *Petak B2*).
   - Foto angka refraktometer menggunakan kamera, pastikan angka Brix terisi otomatis.
   - Isi koordinat Latitude dan Longitude.
   - Klik **Simpan**. Pastikan data baru muncul di Beranda dan tersimpan di database PostgreSQL.

4. **Pengujian Sesi Persisten & Log Out**:
   - Tutup aplikasi E-Brix secara penuh dari *Recent Apps* HP.
   - Buka kembali aplikasi ➡️ Pastikan aplikasi **langsung masuk ke Beranda** tanpa minta login ulang.
   - Klik tombol **Logout** di TopBar ➡️ Pastikan aplikasi kembali ke layar Login.
