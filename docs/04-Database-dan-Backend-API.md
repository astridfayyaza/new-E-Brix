# 🗄️ Bab 4: Database PostgreSQL & Backend Flask (Python)

## 🐘 1. Skema Database PostgreSQL

Database yang digunakan bernama `postgres` (atau `ebrix_db`), dengan struktur tabel utama bernama `scans`:

```sql
CREATE TABLE IF NOT EXISTS scans (
    id SERIAL PRIMARY KEY,
    petak VARCHAR(100) NOT NULL,
    jenis_tebu VARCHAR(100),
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
- `jenis_tebu`: Jenis / varietas tebu (misal: Bululawang, PS 862, KK, dll).
- `image_base64`: String teks hasil konversi gambar refraktometer (format Base64).
- `brix`: Hasil angka kadar kemanisan gula tebu.
- `lat`: Koordinat Latitude lokasi pengambilan sampel.
- `lon`: Koordinat Longitude lokasi pengambilan sampel.
- `timestamp`: Tanggal dan waktu lengkap saat sampel diambil.

---

## 🐍 2. Aplikasi Backend Flask (`app.py`)

Backend dibuat menggunakan bahasa **Python** dengan framework **Flask** dan driver database `psycopg2-binary`.

### Dependensi Python:
- `flask`: Framework web mikro untuk Python.
- `flask-cors`: Middleware untuk mengizinkan akses koneksi lintas domain/network (CORS).
- `psycopg2-binary`: Driver antarmuka PostgreSQL untuk Python.

Perintah Instalasi Library:
```bash
pip install flask flask-cors psycopg2-binary
```

---

## 📑 3. Spesifikasi API Endpoint (Flask)

| Method | Endpoint | Fungsi | Payload Request | Respon Sukses |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | Test status server Flask | *None* | `200 OK` ("Server Backend E-Brix (Flask) berjalan...") |
| `GET` | `/api/scans` | Ambil semua data scan | *None* | `200 OK` (Array JSON berisi daftar objek scan) |
| `POST` | `/api/scans` | Simpan data scan baru | Objek JSON `ScanDto` | `201 Created` (Objek JSON data yang baru disimpan) |
| `PUT` | `/api/scans/<id>` | Memperbarui data scan berdasarkan ID | Objek JSON `ScanDto` | `200 OK` (Objek JSON data yang telah diperbarui) |
| `DELETE` | `/api/scans/<id>` | Hapus data scan berdasarkan ID | *None* | `200 OK` (Pesan sukses hapus) |
