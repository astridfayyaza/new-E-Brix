# 📱 Bab 3: Fitur & Komponen Aplikasi Android E-Brix

## 🏛️ Pola Arsitektur Codebase (MVVM)

Aplikasi E-Brix dibangun menggunakan pola arsitektur **MVVM (Model-View-ViewModel)** dengan *Jetpack Compose*:

- **Model (`data/` & `network/`)**: Berisi struktur data domain (`ScanData`), Data Transfer Object (`ScanDto`), serta interface API (`ApiService`).
- **View (`ui/`)**: Berisi tampilan antarmuka yang dibuat menggunakan *Declarative UI Jetpack Compose* (`LoginScreen`, `HomeScreen`, `FormScreen`, `DetailScreen`).
- **ViewModel (`viewmodel/`)**: Berisi logika bisnis dan manajemen *State* aplikasi (`ScanViewModel` untuk data scan & `AuthViewModel` untuk otentikasi login).

---

## 🎨 Modul UI & Halaman Utama

1. **`LoginScreen.kt`**:
   - Halaman pertama saat pengguna belum melakukan Sign-In.
   - Menampilkan logo tebu, deskripsi aplikasi, serta tombol **"Sign in with Google"**.

2. **`HomeScreen.kt`**:
   - Beranda utama yang menampilkan daftar riwayat pemindaian dari database PostgreSQL.
   - Dilengkapi nama akun pengguna di TopBar, tombol **Refresh** data, dan tombol **Logout**.
   - Menampilkan *loading indicator* saat mengambil data, serta penanganan saat data kosong.

3. **`FormScreen.kt`**:
   - Form untuk menginput data pemindaian baru.
   - Dilengkapi fitur kamera untuk memfoto skala refraktometer, deteksi angka Brix otomatis via OCR, serta input petak, latitude, dan longitude.
   - Tombol **Simpan** akan aktif secara otomatis jika seluruh kolom data telah terisi dengan benar.

4. **`DetailScreen.kt`**:
   - Menampilkan rincian lengkap dari data yang dipilih pengguna, termasuk foto refraktometer, nilai Brix, koordinat GPS, dan waktu scan.

---

## 👁️ Pemrosesan Gambar (OCR ML Kit)

Fungsi ekstraksi teks pada file `ocr/OCRProcessor.kt` memanfaatkan pustaka `com.google.mlkit:text-recognition`. 

Saat foto refraktometer diambil, bitmap gambar diproses oleh `TextRecognition.getClient(...)`. Teks angka yang terdeteksi kemudian difilter secara otomatis untuk mengisi kolom nilai Brix pada form secara presisi.

---

## 🌐 Koneksi Network (Retrofit2 & Failover Interceptor)

Untuk komunikasi ke backend, file `network/ApiService.kt` dilengkapi dengan **Automatic Failover Interceptor**:
- Secara *default*, aplikasi mencoba mengirim request ke port USB ADB Reverse (`http://127.0.0.1:3000/`).
- Jika koneksi USB terputus (*No route to host*), OkHttp secara otomatis mengalihkan request ke IP Wi-Fi Laptop (`http://10.66.178.226:3000/`) tanpa memunculkan pesan error di HP pengguna.
