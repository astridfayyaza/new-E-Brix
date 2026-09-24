# 📝 Bab 7: Catatan Pengembangan & Rencana Fitur Selanjutnya

Dokumen ini mencatat hal-hal yang perlu terus diperbarui seiring berjalannya pengembangan project E-Brix (misalnya saat tugas magang/kuliah bertambah).

---

## 📅 Log Perubahan (Change Log)

### **v1.0.0 (Versi Saat Ini)**
- [x] Integrasi UI Jetpack Compose (LoginScreen, HomeScreen, FormScreen, DetailScreen).
- [x] Deteksi otomatis angka refraktometer menggunakan Google ML Kit Text Recognition (OCR).
- [x] Koneksi REST API Retrofit2 ke server Node.js & Database PostgreSQL.
- [x] Fitur Auto-Failover Interceptor (USB ADB Reverse & IP Wi-Fi Laptop).
- [x] Fitur Otentikasi Google Sign-In dengan penyimpanan sesi persisten di `SharedPreferences`.
- [x] Pembuatan dokumentasi modular di folder `docs/`.

---

## 💡 Rencana Fitur di Masa Mendatang (Future Work)

Berikut adalah beberapa ide dan rencana pengembangan fitur lanjutan yang bisa ditambahkan ke dalam project E-Brix:

1. **Peta Interaktif (GPS Mapping)**:
   - Menampilkan sebaran lokasi petak lahan dan nilai kadar Brix dalam bentuk pin peta interaktif (menggunakan Google Maps SDK atau OpenStreetMap).

2. **Dukungan Mode Offline (Room Database)**:
   - Menyimpan data scan ke database lokal HP (*Room*) terlebih dahulu jika lokasi perkebunan tidak memiliki sinyal seluler, lalu otomatis mensinkronkan data ke PostgreSQL saat kembali online.

3. **Ekspor Laporan (PDF / Excel)**:
   - Menambahkan fitur ekspor rekapitulasi data kadar Brix berdasarkan tanggal atau petak lahan ke dalam file PDF/Excel.

4. **Visualisasi Grafik Tren Kadar Brix**:
   - Menampilkan grafik peningkatan atau penurunan kadar Brix tebu antar minggu/bulan untuk mempermudah penentuan masa panen ideal.

---

> 💡 **Tips untuk Pengembang**: Setiap kali menambahkan fitur baru pada project E-Brix, buatlah file markdown baru di dalam folder `docs/` (misalnya `docs/08-Fitur-Peta-GPS.md`) atau perbarui log pada file ini!
