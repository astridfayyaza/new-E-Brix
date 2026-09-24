# 🍃 Dokumentasi Project E-Brix - Bab 1: Pengenalan & Latar Belakang

## 📌 Apa itu E-Brix?

**E-Brix** adalah aplikasi Android berbasis *Jetpack Compose* yang dibuat untuk membantu petani dan petugas lapangan dalam mengukur serta memantau nilai **Brix** (kadar gula/kemanisan) hasil pertanian, khususnya komoditas tebu.

Di lapangan, pengukuran kadar Brix biasanya dilakukan menggunakan alat *refraktometer*. Namun, pencatatan hasil ukur secara manual sering kali memakan waktu dan berisiko terjadi kesalahan input (*human error*). 

Dengan adanya aplikasi E-Brix, proses pencatatan menjadi otomatis:
1. Petugas cukup memfoto skala pada refraktometer.
2. Aplikasi akan mengekstrak nilai angka Brix secara otomatis menggunakan teknologi **OCR (Optical Character Recognition)** dari Google ML Kit.
3. Data hasil scan (termasuk koordinat lokasi GPS, petak lahan, dan waktu) langsung tersimpan secara *real-time* ke database server **PostgreSQL**.

---

## 🎯 Tujuan Pembuatan Project

- **Meningkatkan Efisiensi Lapangan**: Mempercepat proses pengumpulan data kadar Brix dari lokasi perkebunan.
- **Akurasi Data**: Meminimalisir kesalahan penulisan angka hasil ukur refraktometer.
- **Penyimpanan Terpusat**: Seluruh hasil scan dari berbagai petak lahan tersimpan rapi di dalam database PostgreSQL sehingga mudah dianalisis.
- **Monitoring Real-time**: Memudahkan tim manajemen atau peneliti dalam memantau kualitas tebu dari berbagai area secara *up-to-date*.

---

## 💻 Lingkup Kerja Project

Sistem E-Brix terdiri dari dua bagian utama:
1. **Aplikasi Mobile (Android)**: Frontend mobile tempat pengguna melakukan login dengan akun Google, mengambil foto scan refraktometer, dan melihat riwayat data scan.
2. **Backend REST API & Database**: Server Node.js (Express.js) yang bertindak sebagai penghubung aman antara HP pengguna dengan database PostgreSQL.
