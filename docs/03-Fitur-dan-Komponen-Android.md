# 📱 Bab 3: Fitur & Komponen UI Aplikasi Android E-Brix

## 🎨 Komponen Layar UI (Jetpack Compose Material 3)

1. **`LoginScreen.kt`**:
   - Halaman pertama saat pengguna belum melakukan Sign-In.
   - Menampilkan identitas aplikasi E-Brix dan tombol **"Sign in with Google"** untuk autentikasi pengguna.

2. **`HomeScreen.kt`**:
   - Beranda utama yang menampilkan daftar riwayat pemindaian tebu dalam bentuk kartu (*Card List*).
   - Menampilkan informasi petak lahan, varietas tebu, nilai Brix, serta waktu pencatatan.
   - Dilengkapi tombol profil pengguna, tombol **Refresh**, tombol **Edit**, tombol **Hapus**, dan tombol aksi tambah data (**+**).

3. **`FormScreen.kt`**:
   - Form input dan edit data berbasis Material 3 yang **responsif** menggunakan `BoxWithConstraints`.
   - Menyesuaikan tata letak secara otomatis untuk Smartphone dan Tablet (Portrait & Landscape):
     - **Tampilan Layar Lebar (Tablet/Landscape)**: Menggunakan susunan 2 kolom (*side-by-side*) dengan `Modifier.weight(1f)`.
     - **Tampilan Smartphone (Portrait)**: Menggunakan susunan 1 kolom vertikal yang rapi.
   - Dilengkapi komponen **Dropdown Varietas Tebu** (`ExposedDropdownMenuBox`) dengan pilihan lengkap: Bululawang (BL), PS 862, PS 881, PSJK 922, Kidang Kencana (KK), VMC 76-16, M 442-51, dan Lainnya.

4. **`DetailScreen.kt`**:
   - Menampilkan rincian lengkap dari data scan yang dipilih, meliputi nama petak lahan, varietas tebu, nilai Brix, koordinat GPS (*Latitude* & *Longitude*), dan *Timestamp*.

---

## 💾 Manajemen Cache Persisten Lokal (`ScanViewModel`)

Untuk menjamin bahwa data pilihan varietas tebu pengguna tetap aman dan tidak hilang saat di-refresh:
- `ScanViewModel` memanfaatkan penyimpanan lokal **`SharedPreferences`** (`ebrix_jenis_tebu_cache`).
- Setiap kali pengguna menyimpan atau memperbarui data, varietas tebu disimpan secara persisten di penyimpanan HP.
- Saat aplikasi membaca daftar data, ViewModel secara otomatis mengidentifikasi dan menggabungkan data cache lokal untuk ditampilkan di UI.

---

## 🌐 HTTP Network Client (Retrofit2 & Failover Interceptor)

Pengiriman data dari Android menggunakan pustaka **Retrofit2** yang dilengkapi **Failover Interceptor**:
- Aplikasi mencoba mengirim request ke alamat server utama.
- Jika koneksi terputus, `OkHttpClient` secara otomatis mengalihkan koneksi ke alamat fallback (misal Android Emulator `10.0.2.2:5000` / Localhost) tanpa mengganggu kenyamanan pengguna.
