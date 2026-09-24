# 🗄️ Bab 4: Database PostgreSQL & Backend REST API

## 🐘 1. Skema Database PostgreSQL

Database yang digunakan bernama `postgres` (atau `ebrix_db`), dengan struktur tabel utama bernama `scans`:

```sql
CREATE TABLE IF NOT EXISTS scans (
    id SERIAL PRIMARY KEY,
    petak VARCHAR(100) NOT NULL,
    image_base64 TEXT,
    brix VARCHAR(50) NOT NULL,
    lat VARCHAR(50),
    lon VARCHAR(50),
    timestamp VARCHAR(100)
);
```

### Penjelasan Kolom Tabel `scans`:
- `id`: Primary Key otomatis yang bertambah secara berurutan (*SERIAL*).
- `petak`: Nama atau kode petak lahan perkebunan.
- `image_base64`: String teks hasil konversi gambar refraktometer (format Base64).
- `brix`: Hasil angka kadar kemanisan gula tebu.
- `lat`: Koordinat Latitude lokasi pengambilan sampel.
- `lon`: Koordinat Longitude lokasi pengambilan sampel.
- `timestamp`: Tanggal dan waktu lengkap saat sampel diambil.

---

## 🟢 2. Aplikasi Backend (`server.js`)

Backend dibuat menggunakan **Node.js** dan framework **Express.js** dengan driver database `pg` (node-postgres).

### Dependensi Backend:
- `express`: Framework web server.
- `pg`: Library PostgreSQL client untuk Node.js.
- `cors`: Middleware untuk mengizinkan akses koneksi lintas domain/network.

### Penanganan Payload Gambar Ukuran Besar:
Karena aplikasi mengirimkan gambar dalam bentuk string Base64, server mengonfigurasi batas ukuran request (*body limit*) sebesar **50MB**:

```javascript
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ limit: '50mb', extended: true }));
```

---

## 📑 3. Spesifikasi API Endpoint

| Method | Endpoint | Fungsi | Payload Request | Respon Sukses |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | Test status server | *None* | `200 OK` ("Server E-Brix Backend berjalan...") |
| `GET` | `/api/scans` | Ambil semua data scan | *None* | `200 OK` (Array JSON berisi daftar objek scan) |
| `POST` | `/api/scans` | Simpan data scan baru | Objek JSON `ScanDto` | `201 Created` (Objek JSON data yang baru disimpan) |
