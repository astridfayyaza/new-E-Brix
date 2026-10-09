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

## 🛡️ Pengamanan Credential Git (`.gitignore`)

Untuk menjaga keamanan token dan kunci Google Service:
- File `app/google-services.json` disetel pada `.gitignore` agar tidak ikut ter-push ke repository publik di GitHub.
- File credential tersebut tetap ada secara lokal di komputer pengembang agar proses build aplikasi Android berjalan lancar.

---

## 🚪 Mekanisme Log Out

Saat pengguna menekan ikon **Logout** pada TopBar Beranda atau dialog profil:
1. Method `authViewModel.signOut()` dipanggil.
2. Seluruh data sesi pengguna pada `SharedPreferences` dibersihkan.
3. Klien `GoogleSignInClient.signOut()` dipanggil.
4. Aplikasi mengarahkan navigasi kembali ke `LoginScreen` dan menghapus *backstack* halaman Beranda.
