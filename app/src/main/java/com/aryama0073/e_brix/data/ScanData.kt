package com.aryama0073.e_brix.data

import android.graphics.Bitmap

data class ScanData(
    val id: Int,
    val petak: String,
    val bitmap: Bitmap?,
    val brix: String,
    val lat: String,
    val lon: String,
    val timestamp: String
)