package com.aryama0073.e_brix.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.aryama0073.e_brix.data.ScanData
import com.google.gson.annotations.SerializedName
import java.io.ByteArrayOutputStream

data class ScanApiResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<ScanDto>? = null
)

data class ScanDto(
    @SerializedName("id_data") val idData: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("id_blok") val idBlok: String? = null,
    @SerializedName("petak") val petakRaw: String? = null,
    @SerializedName("foto") val foto: String? = null,
    @SerializedName("image_base64") val imageBase64: String? = null,
    @SerializedName("nilai_brix") val nilaiBrix: Double? = null,
    @SerializedName("brix") val brixRaw: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("lat") val latRaw: String? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("lon") val lonRaw: String? = null,
    @SerializedName("timestamp") val timestampRaw: String? = null
) {
    val petak: String get() = petakRaw ?: idBlok ?: "Blok A"
    val brix: String get() = nilaiBrix?.toString() ?: brixRaw ?: "0"
    val lat: String get() = latitude?.toString() ?: latRaw ?: "0"
    val lon: String get() = longitude?.toString() ?: lonRaw ?: "0"
    val timestamp: String get() = timestampRaw ?: ""
}

fun ScanDto.toDomain(): ScanData {
    val imageStr = foto ?: imageBase64
    val bitmap: Bitmap? = imageStr?.let { str ->
        try {
            if (str.startsWith("http://") || str.startsWith("https://")) {
                null
            } else {
                val decodedBytes = Base64.decode(str, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            }
        } catch (e: Exception) {
            null
        }
    }

    val parsedId = id ?: (idData.hashCode() and 0x7FFFFFFF)

    return ScanData(
        id = parsedId,
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
        petakRaw = petak,
        imageBase64 = base64String,
        nilaiBrix = brix.toDoubleOrNull(),
        brixRaw = brix,
        latitude = lat.toDoubleOrNull(),
        latRaw = lat,
        longitude = lon.toDoubleOrNull(),
        lonRaw = lon,
        timestampRaw = timestamp
    )
}
