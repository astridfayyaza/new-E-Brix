# 🍃 Dokumentasi Android E-Brix - Bab 1: Pengenalan & Latar Belakang

## 📌 Apa itu Aplikasi Android E-Brix?

**E-Brix** adalah aplikasi mobile Android berbasis **Jetpack Compose** yang dikembangkan untuk memudahkan petugas dan tim lapangan dalam menginput, mengelola, serta memantau nilai **Brix** (kadar gula/kemanisan) dan **varietas tebu** dari berbagai petak lahan perkebunan.

Aplikasi ini menggantikan proses pencatatan manual di lapangan menjadi serba digital dan terstruktur:
1. Petugas menginput informasi petak/blok lahan tebu.
2. Petugas memilih jenis/varietas tebu (Bululawang, PS 862, KK, VMC, dll) melalui dropdown Material 3.
3. Petugas menginput nilai kadar Brix (%) serta koordinat lokasi GPS (*Latitude* & *Longitude*).
4. Aplikasi menyimpan data ke cache persisten lokal dan mengunggahnya ke server melalui koneksi REST API secara *real-time*.

---

## 🎯 Tujuan Pengembangan Aplikasi

- **Digitalisasi Input Data Lapangan**: Mempercepat proses pencatatan kadar Brix dan varietas tebu secara rapi dan sistematis.
- **Akurasi & Aksesibilitas Data**: Meminimalisir kesalahan input manual serta memudahkan peninjauan ulang data pemindaian tebu dari HP.
- **Tampilan Modern & Responsif**: Menggunakan standar Material 3 (*Material You*) dengan layout adaptif untuk perangkat Smartphone maupun Tablet.
- **Keandalan Akses Data**: Dilengkapi cache persisten lokal (`SharedPreferences`) sehingga data pilihan pengguna tetap aman dan tidak hilang.

---

## 💻 Lingkup Pengembangan Android

Pengembangan project ini berfokus pada **Aplikasi Mobile Android (Client)**:
- **Tampilan Antarmuka (UI)**: Halaman Login Google, Beranda Daftar Scan, Form Input/Edit Data Responsif, serta Halaman Detail Data.
- **Manajemen State & Logika**: Penggunaan ViewModel dan StateFlow untuk menangani alur data aplikasi.
- **Konektivitas Network**: Pengiriman dan pengambilan data menggunakan Retrofit2 REST API Client.
