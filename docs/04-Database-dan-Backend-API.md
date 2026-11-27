# 🌐 Bab 4: Model Data Domain & Integrasi REST API Client

## 📦 1. Model Data Domain Android (`ScanData.kt`)

Model data domain internal yang digunakan oleh aplikasi Android E-Brix untuk merepresentasikan 1 entitas data pemindaian tebu:

```kotlin
data class ScanData(
    val id: Int,
    val petak: String,
    val jenisTebu: String = "Bululawang (BL)",
    val brix: String,
    val lat: String,
    val lon: String,
    val timestamp: String,
)
```

### Penjelasan Variabel:
- `id`: Angka identitas unik dari setiap data scan.
- `petak`: Nama atau kode petak lahan perkebunan tebu.
- `jenisTebu`: Varietas tebu pilihan pengguna (default: `"Bululawang (BL)"`).
- `brix`: Angka kadar kemanisan gula tebu (%).
- `lat`: Koordinat Latitude GPS lokasi pengambilan data.
- `lon`: Koordinat Longitude GPS lokasi pengambilan data.
- `timestamp`: Tanggal dan waktu lengkap saat data disimpan.

---

## 📄 2. Data Transfer Object (`ScanDto.kt`)

`ScanDto` bertugas menjembatani format data dari REST API Client (format JSON) dengan model data internal Android (`ScanData`).

```kotlin
data class ScanDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("petak") val petakRaw: String? = null,
    @SerializedName("jenis_tebu") val jenisTebuRaw: String? = null,
    @SerializedName("brix") val brixRaw: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("timestamp") val timestampRaw: String? = null,
)
```

### Fungsi Konversi Data:
- **`toDomain()`**: Mengonversi objek JSON `ScanDto` dari API menjadi objek internal `ScanData`.
- **`toDto()`**: Mengonversi objek internal `ScanData` dari HP Android menjadi payload `ScanDto` untuk dikirimkan ke REST API.

---

## 🔌 3. Interface Retrofit HTTP Client (`ApiService.kt`)

Aplikasi Android menggunakan interface Retrofit `ApiService` untuk mengeksekusi panggilan HTTP:

| HTTP Method | Endpoint API | Fungsi di Android | Payload / Param |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/scans` | Mengambil seluruh daftar data scan | *None* |
| `POST` | `/api/scans` | Mengirim data scan tebu baru | Payload `ScanDto` |
| `PUT` | `/api/scans/{id}` | Mengubah data scan berdasarkan ID | Payload `ScanDto` |
| `DELETE` | `/api/scans/{id}` | Menghapus data scan berdasarkan ID | Path `id` |
