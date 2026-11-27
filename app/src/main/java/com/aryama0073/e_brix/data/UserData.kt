package com.aryama0073.e_brix.data

data class UserData(
    val email: String,
    val displayName: String?,
    val photoUrl: String? = null,
    val role: String = "petugas_lapangan", // "petugas_lapangan" atau "petani"
    val token: String? = null
) {
    val isPetugas: Boolean get() = role.contains("petugas", ignoreCase = true)
    val roleLabel: String get() = if (isPetugas) "Petugas Lapangan PT Rajawali" else "Petani Mitra"
}
