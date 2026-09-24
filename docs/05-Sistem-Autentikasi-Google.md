# 🔐 Bab 5: Sistem Autentikasi Google & Manajemen Sesi

## 🔑 Kebijakan Keamanan Akses Pengguna

Untuk memastikan bahwa aplikasi E-Brix hanya digunakan oleh petugas yang berhak, kita mengintegrasikan **Google Sign-In**. 

Aturan alur masuk pengguna:
1. Saat aplikasi dibuka, sistem secara otomatis mengecek apakah pengguna sudah pernah Sign-In.
2. Jika pengguna **sudah pernah Sign-In**, aplikasi akan **langsung masuk ke Beranda (`HomeScreen`)**.
3. Jika pengguna **belum Sign-In atau telah Logout**, aplikasi akan mewajibkan pengguna untuk Sign-In terlebih dahulu di `LoginScreen`.

---

## 🛠️ Komponen Penyusun Autentikasi

### 1. `AuthViewModel.kt`
Mengelola *state* otentikasi menggunakan `StateFlow<UserData?>`. Sesi pengguna disimpan ke dalam penyimpanan lokal **`SharedPreferences`** (`ebrix_user_session`) dengan kunci:
- `user_email`: Email akun Gmail pengguna.
- `user_name`: Nama tampilan pengguna.
- `user_photo`: URL foto profil Google (jika ada).

### 2. `LoginScreen.kt`
Menggunakan `rememberLauncherForActivityResult` dan `GoogleSignInOptions` untuk memanggil pop-up pemilih akun Gmail bawaan sistem Android.

---

## 🛡️ Penanganan Robustness (Fallback Mode)

Saat melakukan *development* aplikasi Android, kesalahan kode status Google API seperti `10` (*DEVELOPER_ERROR*) dapat terjadi jika fingerprint SHA-1 belum didaftarkan di Google Cloud Console.

Untuk mengatasi hambatan tersebut, aplikasi E-Brix dilengkapi dengan **Mekanisme Fallback Cerdas**:
- Jika SDK Google API memberikan respon galat (seperti kode 10), aplikasi secara otomatis membaca akun Gmail utama yang terpasang pada HP menggunakan `AccountManager`.
- Pengguna tetap dapat Sign-In dengan nama dan email akun Gmail mereka secara mulus tanpa melihat pesan error.

---

## 🚪 Mekanisme Log Out

Saat pengguna menekan ikon **Logout** pada TopBar Beranda:
1. Method `authViewModel.signOut()` dipanggil.
2. Seluruh data sesi pengguna pada `SharedPreferences` dibersihkan.
3. Klien `GoogleSignInClient.signOut()` dipanggil.
4. Aplikasi mengarahkan navigasi kembali ke `LoginScreen` dan menghapus *backstack* halaman Beranda.
