package com.aryama0073.e_brix.network

import com.aryama0073.e_brix.data.ScanData
import com.google.gson.annotations.SerializedName

data class ScanApiResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<ScanDto>? = null,
)

data class ScanDto(
    @SerializedName("id_data") val idData: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("id_blok") val idBlok: String? = null,
    @SerializedName("petak") val petakRaw: String? = null,
    @SerializedName("jenis_tebu") val jenisTebuRaw: String? = null,
    @SerializedName("varietas") val varietasRaw: String? = null,
    @SerializedName("nilai_brix") val nilaiBrix: Double? = null,
    @SerializedName("brix") val brixRaw: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("lat") val latRaw: String? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("lon") val lonRaw: String? = null,
    @SerializedName("timestamp") val timestampRaw: String? = null,
) {
    val petak: String get() = petakRaw ?: idBlok ?: "Blok A"
    val jenisTebu: String get() = jenisTebuRaw ?: varietasRaw ?: "Bululawang (BL)"
    val brix: String get() = nilaiBrix?.toString() ?: brixRaw ?: "0"
    val lat: String get() = latitude?.toString() ?: latRaw ?: "0"
    val lon: String get() = longitude?.toString() ?: lonRaw ?: "0"
    val timestamp: String get() = timestampRaw ?: ""
}

fun ScanDto.toDomain(): ScanData {
    val parsedId = id ?: (idData.hashCode() and 0x7FFFFFFF)

    return ScanData(
        id = parsedId,
        petak = petak,
        jenisTebu = jenisTebu,
        brix = brix,
        lat = lat,
        lon = lon,
        timestamp = timestamp,
    )
}

fun ScanData.toDto(): ScanDto {
    return ScanDto(
        id = if (id != 0) id else null,
        petakRaw = petak,
        jenisTebuRaw = jenisTebu,
        nilaiBrix = brix.toDoubleOrNull(),
        brixRaw = brix,
        latitude = lat.toDoubleOrNull(),
        latRaw = lat,
        longitude = lon.toDoubleOrNull(),
        lonRaw = lon,
        timestampRaw = timestamp,
    )
}
