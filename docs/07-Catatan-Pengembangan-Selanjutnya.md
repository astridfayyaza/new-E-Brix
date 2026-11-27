# 📝 Bab 7: Catatan Pengembangan & Rencana Fitur Selanjutnya

Dokumen ini mencatat log pengubahan dan rencana pengembangan lanjutan untuk aplikasi Android E-Brix.

---

## 📅 Log Perubahan (Change Log)

### **v1.1.0 (Versi Terbaru)**
- [x] Fitur pemilihan **Jenis/Varietas Tebu** (Bululawang, PS 862, KK, VMC, dll) menggunakan Dropdown Material 3.
- [x] Refactoring UI **`FormScreen.kt`** menjadi responsif (Smartphone & Tablet) menggunakan `BoxWithConstraints` dan Material 3 Cards.
- [x] Implementasi **Cache Persisten `SharedPreferences`** pada `ScanViewModel` untuk menyimpan jenis tebu secara permanen.
- [x] Pengamanan file credential **`google-services.json`** pada `.gitignore` dan pelepasan pelacakan dari Git repository.
- [x] Perbaikan handler Google Sign-In di `LoginScreen.kt` untuk penanganan error code yang bersih.
- [x] Penyederhanaan form input tanpa fitur foto/OCR untuk memfokuskan alur pada pencatatan varietas dan nilai Brix.

### **v1.0.0 (Versi Awal)**
- [x] Integrasi UI Jetpack Compose (LoginScreen, HomeScreen, FormScreen, DetailScreen).
- [x] Koneksi REST API Retrofit2 & Gson Converter.
- [x] Fitur Auto-Failover Interceptor jaringan.
- [x] Fitur Otentikasi Google Sign-In dengan penyimpanan sesi persisten di `SharedPreferences`.
- [x] Pembuatan dokumentasi modular di folder `docs/`.

---

## 💡 Rencana Pengembangan Fitur Selanjutnya (Future Work)

Berikut adalah beberapa rencana pengembangan fitur lanjutan pada aplikasi Android E-Brix:

1. **Peta Interaktif (GPS Mapping)**:
   - Menampilkan lokasi petak lahan, varietas tebu, dan nilai kadar Brix dalam bentuk pin peta interaktif menggunakan Google Maps SDK.

2. **Dukungan Mode Offline (Room Database)**:
   - Menyimpan data scan ke database lokal HP (*Room*) saat tidak ada koneksi internet, lalu otomatis sinkronkan saat kembali online.

3. **Ekspor Laporan (PDF / Excel)**:
   - Menambahkan fitur ekspor rekapitulasi data kadar Brix dan varietas tebu ke dalam file PDF atau Excel.

4. **Visualisasi Grafik Tren Kadar Brix**:
   - Menampilkan grafik peningkatan atau penurunan kadar Brix tebu antar minggu/bulan untuk mempermudah penentuan masa panen ideal.

---

> 💡 **Tips Pengembang**: Setiap kali menambahkan fitur baru pada project Android E-Brix, perbarui log perubahan pada dokumen ini!
