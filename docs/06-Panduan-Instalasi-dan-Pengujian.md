# 🚀 Bab 6: Panduan Build & Pengujian Aplikasi Android

## 📋 Prasyarat Perangkat Lunak

Sebelum menjalankan project Android **E-Brix**, pastikan perangkat komputer/laptop Anda telah terpasang:
- **Android Studio** (Versi Jellyfish / Ladybug atau yang terbaru).
- **JDK 17** atau yang terbaru.
- **Android SDK** (API Level 24 / Android 7.0 hingga Android 14+).
- **Perangkat Pengujian**: Android Emulator atau HP Android fisik dengan mode **USB Debugging** diaktifkan.

---

## 🛠️ Langkah-Langkah Build & Running Project

### Langkah 1: Buka Project di Android Studio
1. Buka aplikasi **Android Studio**.
2. Pilih menu **Open** dan arahkan ke folder project `E-Brix-Android`.
3. Tunggu proses **Gradle Sync** selesai hingga seluruh dependensi terunduh dengan sempurna.

### Langkah 2: Konfigurasi File Credential Google
1. Pastikan file `google-services.json` telah berada di dalam folder `app/` (`E-Brix-Android/app/google-services.json`).
2. Jika belum ada, letakkan file `google-services.json` dari proyek Firebase/Google Cloud Anda ke dalam folder `app/`.

### Langkah 3: Aktifkan Port Forwarding USB (Khusus HP Fisik USB)
Buka PowerShell di komputer Anda, lalu jalankan perintah berikut agar port `5000` di HP diteruskan ke laptop Anda:

```powershell
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -d reverse tcp:5000 tcp:5000
```

### Langkah 4: Jalankan Aplikasi pada Emulator / HP Fisik
1. Pilih target perangkat (*Android Emulator* atau *HP Fisik via USB*).
2. Klik tombol **Run ▶️** (hijau) di bagian toolbar Android Studio.
3. Aplikasi akan dikompilasi (`app:assembleDebug`) dan dipasang pada perangkat target.

---

## 🧪 Skenario Pengujian Aplikasi (Testing Steps)

1. **Pengujian Google Sign-In**:
   - Saat aplikasi pertama kali dibuka, pastikan halaman `LoginScreen` tampil.
   - Klik **"Sign in with Google"**, pilih akun Google Anda.
   - Pastikan muncul pesan Toast *"Selamat datang, [Nama]!"* dan layar berpindah ke Beranda.

2. **Pengujian Tambah Data Scan Baru**:
   - Tekan tombol **`+`** (Floating Action Button) pada Beranda.
   - Isi petak lahan (misal: *Petak B2*).
   - Pilih varietas tebu dari dropdown (misal: *Bululawang (BL)*).
   - Isi nilai Brix (%), Latitude, dan Longitude.
   - Klik **Simpan**. Pastikan data baru tampil pada daftar Beranda.

3. **Pengujian Edit & Persistensi Cache Varietas Tebu**:
   - Tekan ikon **Edit** pada salah satu item data di Beranda.
   - Ubah varietas tebu dari dropdown (misal ke *PS 862*) dan klik **Simpan Perubahan**.
   - Pastikan varietas tebu pada Beranda dan Halaman Detail berubah menjadi *PS 862* dan tidak kembali ke nilai default.

4. **Pengujian Tampilan Responsif (Smartphone & Tablet)**:
   - Jalankan aplikasi pada perangkat tablet atau putar layar ke posisi **Landscape**.
   - Buka `FormScreen` dan pastikan tata letak form berubah secara adaptif menjadi 2 kolom berdampingan (*side-by-side*).

5. **Pengujian Logout & Sesi Persisten**:
   - Tutup aplikasi secara penuh dari *Recent Apps* HP.
   - Buka kembali aplikasi ➡️ Pastikan aplikasi **langsung masuk ke Beranda** tanpa minta login ulang.
   - Buka dialog profil dan klik **Logout** ➡️ Pastikan aplikasi kembali ke layar Login.
