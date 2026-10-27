package com.aryama0073.e_brix.data

data class ScanData(
    val id: Int,
    val petak: String,
    val jenisTebu: String = "Bululawang (BL)",
    val brix: String,
    val lat: String,
    val lon: String,
    val timestamp: String,
)
