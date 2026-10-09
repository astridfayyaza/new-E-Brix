# 📱 Bab 3: Fitur & Komponen Aplikasi Android E-Brix

## 🏛️ Pola Arsitektur Codebase (MVVM)

Aplikasi E-Brix dibangun menggunakan pola arsitektur **MVVM (Model-View-ViewModel)** dengan *Jetpack Compose*:

- **Model (`data/` & `network/`)**: Berisi struktur data domain (`ScanData`), Data Transfer Object (`ScanDto`), serta interface API (`ApiService`).
- **View (`ui/`)**: Berisi tampilan antarmuka yang dibuat menggunakan *Declarative UI Jetpack Compose* (`LoginScreen`, `HomeScreen`, `FormScreen`, `DetailScreen`).
- **ViewModel (`viewmodel/`)**: Berisi logika bisnis, cache lokal `SharedPreferences`, dan manajemen *State* aplikasi (`ScanViewModel` untuk data scan & `AuthViewModel` untuk otentikasi login).

---

## 🎨 Modul UI & Halaman Utama

1. **`LoginScreen.kt`**:
   - Halaman pertama saat pengguna belum melakukan Sign-In.
   - Menampilkan logo tebu, deskripsi aplikasi, serta tombol **"Sign in with Google"**.
   - Penanganan error login yang aman dan informatif tanpa fallback akun keras.

2. **`HomeScreen.kt`**:
   - Beranda utama yang menampilkan daftar riwayat pemindaian, petak lahan, jenis tebu, dan nilai Brix.
   - Dilengkapi profil akun pengguna, tombol **Refresh** data, serta tombol **Edit**, **Hapus**, dan **Logout**.

3. **`FormScreen.kt`**:
   - Form responsif berbasis Material 3 (`BoxWithConstraints`) yang mendukung tampilan Smartphone & Tablet (Portrait & Landscape).
   - Mengelompokkan input ke dalam kartu-kartu estetik: Informasi Lahan & Varietas, Foto Sampel & OCR, serta Hasil Pengukuran & GPS.
   - Dilengkapi dropdown pilihan **Jenis Tebu (Varietas)** seperti Bululawang (BL), PS 862, PS 881, PSJK 922, Kidang Kencana (KK), VMC 76-16, M 442-51, dll.
   - Fitur kamera untuk memfoto skala refraktometer dan otomatis membaca angka Brix via OCR.

4. **`DetailScreen.kt`**:
   - Menampilkan rincian lengkap dari data yang dipilih pengguna, termasuk foto refraktometer, jenis tebu, nilai Brix, koordinat GPS, dan waktu scan.

---

## 👁️ Pemrosesan Gambar (OCR ML Kit)

Fungsi ekstraksi teks pada file `ocr/OCRProcessor.kt` memanfaatkan pustaka `com.google.mlkit:text-recognition`. 

Saat foto refraktometer diambil, bitmap gambar diproses oleh `TextRecognition.getClient(...)`. Teks angka yang terdeteksi kemudian difilter secara otomatis untuk mengisi kolom nilai Brix pada form secara presisi.

---

## 💾 Manajemen Cache Lokal Jenis Tebu (`ScanViewModel`)

Untuk memastikan bahwa data pilihan varietas tebu dari pengguna tidak hilang saat di-edit atau di-refresh, `ScanViewModel` dilengkapi dengan **Cache Persisten (`SharedPreferences`)**:
- Setiap kali data ditambahkan atau diperbarui, pilihan jenis tebu disimpan ke cache lokal `ebrix_jenis_tebu_cache`.
- Saat data diambil ulang dari server database, ViewModel menggabungkan data server dengan cache lokal agar nilai varietas tebu tetap aman dan konsisten.

---

## 🌐 Koneksi Network (Retrofit2 & Failover Interceptor)

Untuk komunikasi ke backend, file `network/ApiService.kt` dilengkapi dengan **Automatic Failover Interceptor**:
- Aplikasi mencoba mengirim request ke IP server utama (`http://10.20.112.60:5000/`).
- Jika koneksi terputus, OkHttp secara otomatis mengalihkan request ke IP Android Emulator (`http://10.0.2.2:5000/`) atau Localhost tanpa mengganggu kenyamanan pengguna.
