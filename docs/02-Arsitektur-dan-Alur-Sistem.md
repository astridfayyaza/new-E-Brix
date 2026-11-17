# 📐 Bab 2: Arsitektur MVVM & Alur Data Aplikasi Android

## 🏗️ Pola Arsitektur MVVM (Model-View-ViewModel)

Aplikasi Android E-Brix dibangun menggunakan pola arsitektur **MVVM (Model-View-ViewModel)** yang direkomendasikan oleh Google untuk memastikan kode rapi, mudah dipelihara, dan memiliki pemisahan tugas (*separation of concerns*) yang jelas:

```text
  [ View Layer (Jetpack Compose UI) ]
                │
                │  (Observasi State & Trigger Event)
                ▼
  [ ViewModel Layer (ScanViewModel & AuthViewModel) ]
                │
                ├──► [ Local Cache (SharedPreferences) ]
                │
                ▼  (Request HTTP / JSON via Retrofit2)
  [ Network Layer (ApiService & ScanDto) ]
```

---

## 🔀 Komponen Arsitektur Android

1. **View Layer (`ui/`)**:
   - Berisi komponen antarmuka *Declarative UI Jetpack Compose* (`HomeScreen`, `FormScreen`, `DetailScreen`, `LoginScreen`).
   - Bertugas menampilkan data berdasarkan *State* dari ViewModel dan meneruskan aksi pengguna (seperti klik simpan/edit).

2. **ViewModel Layer (`viewmodel/`)**:
   - Menampung data sementara menggunakan `StateFlow`.
   - Mengelola logika bisnis aplikasi, seperti pemrosesan tambah/edit/hapus data scan dan penyimpanan cache persisten ke `SharedPreferences`.

3. **Data & Network Layer (`data/` & `network/`)**:
   - `ScanData`: Model data domain internal Android.
   - `ScanDto`: Data Transfer Object untuk pertukaran data JSON.
   - `ApiService`: Interface HTTP Client berbasis Retrofit2 untuk komunikasi data luar.

---

## 🔄 Alur Perjalanan Data (Data Flow) dalam Aplikasi

1. **Penginputan Data di UI**:
   - Pengguna mengisi data petak lahan, varietas tebu, nilai Brix, dan lokasi pada `FormScreen`.

2. **Pengolahan di ViewModel**:
   - `FormScreen` memanggil fungsi `viewModel.addData()` atau `viewModel.updateData()`.
   - `ScanViewModel` menyimpan nilai varietas tebu ke cache lokal `SharedPreferences` agar data aman secara persisten.

3. **Pengiriman via REST API Client**:
   - `ScanViewModel` mengonversi `ScanData` menjadi `ScanDto` lalu memanggil `ApiService` via Retrofit2 (`POST` atau `PUT`).

4. **Pembaruan State UI**:
   - Setelah mendapatkan respon sukses, `ScanViewModel` memperbarui `_dataList` (`StateFlow`).
   - `HomeScreen` dan `DetailScreen` secara otomatis merefresh tampilan UI sesuai data terbaru.
