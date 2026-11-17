# 🔐 Bab 5: Sistem Autentikasi Google & Manajemen Sesi

## 🔑 Kebijakan Keamanan Akses Pengguna

Untuk memastikan bahwa aplikasi E-Brix hanya dapat diakses oleh petugas yang memiliki otorisasi, aplikasi diintegrasikan dengan **Google Sign-In**.

Aturan alur masuk pengguna:
1. Saat aplikasi dibuka, sistem secara otomatis mengecek status login pengguna di `SharedPreferences`.
2. Jika pengguna **sudah login**, aplikasi akan **langsung masuk ke Beranda (`HomeScreen`)**.
3. Jika pengguna **belum login atau telah Logout**, aplikasi akan mengarahkan pengguna ke `LoginScreen`.

---

## 🛠️ Komponen Autentikasi Android

### 1. `AuthViewModel.kt`
Mengelola *state* otentikasi menggunakan `StateFlow<UserData?>`. Sesi pengguna disimpan secara persisten di penyimpanan lokal **`SharedPreferences`** (`ebrix_user_session`) dengan kunci:
- `user_email`: Email akun Google pengguna.
- `user_name`: Nama tampilan pengguna.
- `user_photo`: URL foto profil Google pengguna.

### 2. `LoginScreen.kt`
Menggunakan `rememberLauncherForActivityResult` dan `GoogleSignInOptions` untuk memanggil dialog pemilih akun Google bawaan sistem Android.

---

## 🛡️ Pengamanan Credential Git (`.gitignore`)

Untuk menjaga keamanan token dan konfigurasi Google Service:
- File `app/google-services.json` dimasukkan ke dalam `.gitignore` agar file sensitif ini tidak ter-push ke repository publik GitHub.
- File tersebut tetap ada di komputer pengembang secara lokal untuk kebutuhan build aplikasi Android.

---

## 🚪 Mekanisme Log Out

Saat pengguna menekan tombol **Logout** pada dialog profil:
1. Method `authViewModel.signOut()` dipanggil.
2. Seluruh data sesi pengguna pada `SharedPreferences` dibersihkan.
3. Klien `GoogleSignInClient.signOut()` dipanggil.
4. Aplikasi mengarahkan navigasi kembali ke `LoginScreen` dan menghapus *backstack* halaman Beranda.
