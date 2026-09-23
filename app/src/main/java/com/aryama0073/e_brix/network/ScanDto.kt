package com.aryama0073.e_brix.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.aryama0073.e_brix.data.ScanData
import com.google.gson.annotations.SerializedName
import java.io.ByteArrayOutputStream

data class ScanDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("petak") val petak: String,
    @SerializedName("image_base64") val imageBase64: String? = null,
    @SerializedName("brix") val brix: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("timestamp") val timestamp: String
)

fun ScanDto.toDomain(): ScanData {
    val bitmap: Bitmap? = imageBase64?.let { base64Str ->
        try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    return ScanData(
        id = id ?: 0,
        petak = petak,
        bitmap = bitmap,
        brix = brix,
        lat = lat,
        lon = lon,
        timestamp = timestamp
    )
}

fun ScanData.toDto(): ScanDto {
    val base64String: String? = bitmap?.let { bmp ->
        try {
            val outputStream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val byteArray = outputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    return ScanDto(
        id = if (id != 0) id else null,
        petak = petak,
        imageBase64 = base64String,
        brix = brix,
        lat = lat,
        lon = lon,
        timestamp = timestamp
    )
}
