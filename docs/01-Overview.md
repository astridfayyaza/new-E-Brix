# 🍃 Dokumentasi Project E-Brix - Bab 1: Pengenalan & Latar Belakang

## 📌 Apa itu E-Brix?

**E-Brix** adalah aplikasi Android berbasis *Jetpack Compose* yang dibuat untuk membantu petani dan petugas lapangan dalam mengukur serta memantau nilai **Brix** (kadar gula/kemanisan) dan **varietas tebu** hasil pertanian secara efisien.

Di lapangan, pengukuran kadar Brix dilakukan menggunakan alat *refraktometer*. Namun, pencatatan hasil ukur dan varietas tebu secara manual sering kali memakan waktu dan berisiko terjadi kesalahan input (*human error*). 

Dengan adanya aplikasi E-Brix, proses pencatatan menjadi otomatis:
1. Petugas memfoto skala pada refraktometer.
2. Aplikasi mengekstrak nilai angka Brix secara otomatis menggunakan teknologi **OCR (Optical Character Recognition)** dari Google ML Kit.
3. Petugas memilih jenis/varietas tebu melalui dropdown Material 3 (Bululawang, PS 862, KK, dll).
4. Data hasil scan (termasuk varietas tebu, koordinat lokasi GPS, petak lahan, dan waktu) langsung tersimpan secara *real-time* ke database server **PostgreSQL**.

---

## 🎯 Tujuan Pembuatan Project

- **Meningkatkan Efisiensi Lapangan**: Mempercepat proses pengumpulan data kadar Brix dan varietas tebu dari lokasi perkebunan.
- **Akurasi Data**: Meminimalisir kesalahan penulisan angka hasil ukur refraktometer.
- **Identifikasi Varietas Tebu**: Memudahkan pemetaan kualitas kadar Brix berdasarkan varietas tebu yang ditanam.
- **Penyimpanan Terpusat & Cache Lokal**: Seluruh hasil scan dari berbagai petak lahan tersimpan rapi di database dan didukung cache lokal persisten.
- **Monitoring Real-time & Tampilan Responsif**: Memudahkan tim manajemen memantau kualitas tebu di berbagai perangkat (Smartphone & Tablet).

---

## 💻 Lingkup Kerja Project

Sistem E-Brix terdiri dari dua bagian utama:
1. **Aplikasi Mobile (Android)**: Frontend mobile tempat pengguna melakukan login dengan akun Google, mengambil foto scan refraktometer, memilih varietas tebu, dan melihat riwayat data scan.
2. **Backend REST API & Database**: Server Python (Flask) / Node.js yang bertindak sebagai penghubung aman antara HP pengguna dengan database PostgreSQL.
