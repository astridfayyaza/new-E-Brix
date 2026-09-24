# 📐 Bab 2: Arsitektur & Alur Kerja Sistem E-Brix

## 🏗️ Gambaran Umum Arsitektur

Agar aplikasi E-Brix aman, stabil, dan dapat diakses oleh banyak pengguna tanpa membocorkan credential database, kita menggunakan arsitektur 3-Tier (Client - Backend API - Database):

```text
  [ Client (HP Android) ]
             │
             │  (Request HTTP / JSON via Retrofit2)
             ▼
  [ REST API (Node.js & Express) ]
             │
             │  (Query SQL via Driver pg)
             ▼
  [ Database (PostgreSQL) ]
```

---

## 🔄 Mengapa Menggunakan REST API alih-alih Koneksi Langsung ke Database?

Saat mengembangkan aplikasi mobile, menghubungkan HP Android langsung ke database PostgreSQL (menggunakan driver JDBC) sangat **tidak disarankan** karena beberapa alasan penting:
1. **Keamanan (Security)**: Password database tidak boleh disimpan di dalam file APK aplikasi karena mudah didekompilasi oleh peretas.
2. **Koneksi Jaringan**: Port default PostgreSQL (`5432`) berbahaya jika dibuka langsung ke internet publik.
3. **Kestabilan Mobile**: Sinyal HP sering kali tidak stabil. REST API menggunakan protokol HTTP yang jauh lebih tahan terhadap pemutusan koneksi singkat dibanding koneksi socket database mentah.

---

## 🔀 Alur Data Aplikasi (Data Flow)

Berikut adalah alur perjalanan data dari saat pengguna melakukan pemindaian hingga tersimpan di server:

1. **Pengambilan Gambar & OCR**:
   - Pengguna membuka halaman form tambah scan dan memfoto refraktometer.
   - Gambar diproses oleh `ML Kit Text Recognition` untuk mendapatkan nilai angka Brix secara otomatis.

2. **Pengumpulan Metadata**:
   - Sistem mengambil nama petak lahan, koordinat GPS (Latitude/Longitude), serta waktu saat scan dilakukan (*timestamp*).
   - Gambar diubah (*compress & encode*) menjadi format string **Base64** agar mudah dikirimkan melalui JSON.

3. **Pengiriman Data via API**:
   - ViewModel memanggil fungsi `ApiService.createScan()` menggunakan Retrofit.
   - Request HTTP `POST /api/scans` dikirimkan ke server backend Node.js.

4. **Penyimpanan di Database**:
   - Server Node.js menerima payload JSON, memvalidasi isinya, lalu menjalankan perintah `INSERT INTO scans ...` ke database PostgreSQL.
   - Database mengembalikan data hasil simpan dengan ID unik baru ke server, dan server meneruskannya kembali ke aplikasi Android.

5. **Pembaruan Tampilan UI**:
   - Aplikasi menerima respon sukses dari server dan merefresh daftar data pada halaman Beranda (*HomeScreen*).
