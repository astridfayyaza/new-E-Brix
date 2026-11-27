package com.aryama0073.e_brix.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryama0073.e_brix.viewmodel.ScanViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: ScanViewModel,
    id: Int,
    onEditClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val dataList by viewModel.dataList.collectAsState()
    val data = dataList.find { it.id == id }

    val greenColor = Color(0xFF059669)
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog && data != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Data") },
            text = { Text("Apakah Anda yakin ingin menghapus data petak '${data.petak}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val deleteId = data.id
                        showDeleteDialog = false
                        viewModel.deleteData(deleteId) {
                            onBack()
                        }
                    }
                ) {
                    Text("Hapus", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail & Hasil Prediksi", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    if (data != null) {
                        IconButton(onClick = { onEditClick(data.id) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Data",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus Data",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        if (data != null) {
            val brixValue = data.brix.toDoubleOrNull() ?: 0.0
            val estimatedRendemen = brixValue * 0.70

            val (maturityStatus, maturityColor, maturityDesc) = when {
                brixValue < 16.0 -> Triple(
                    "Fase Pertumbuhan (Belum Masak)",
                    Color(0xFFD97706),
                    "Akumulasi sukrosa masih rendah. Disarankan menunggu 2-4 minggu lagi sebelum panen."
                )
                brixValue in 16.0..18.0 -> Triple(
                    "Awal Masak (Masa Pembentukan)",
                    Color(0xFF2563EB),
                    "Kadar sukrosa mulai optimal. Lakukan pengecekan berkala tiap 7 hari."
                )
                brixValue in 18.01..21.5 -> Triple(
                    "Kematangan Optimal (Siap Tebang)",
                    Color(0xFF059669),
                    "Kondisi tebu sangat prima! Rekomendasi segera ditebang dan dikirim ke PG Rajawali."
                )
                else -> Triple(
                    "Lewat Masak (Overripe)",
                    Color(0xFFDC2626),
                    "Tebu sudah lewat masa optimum. Segera giling untuk mencegah inversi sukrosa."
                )
            }

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF8FAFC))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Kartu Informasi Lahan
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Eco, contentDescription = null, tint = greenColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Informasi Petak & Tanaman", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Petak / Blok Lahan:", color = Color.Gray, fontSize = 14.sp)
                            Text(data.petak, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Varietas Tebu:", color = Color.Gray, fontSize = 14.sp)
                            Text(data.jenisTebu, fontWeight = FontWeight.SemiBold, color = greenColor, fontSize = 14.sp)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Waktu Scan:", color = Color.Gray, fontSize = 14.sp)
                            Text(data.timestamp.ifBlank { "-" }, fontSize = 13.sp)
                        }
                    }
                }

                // Kartu Hasil Pengukuran Brix & Geotagging
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = greenColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hasil Pemindaian Refraktometer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(greenColor.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Nilai Kadar Brix Terdeteksi", fontSize = 12.sp, color = Color.Gray)
                                Text("${data.brix} %", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = greenColor)
                            }

                            Surface(
                                color = greenColor,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Refraktometer OCR", color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Geotagging GPS: Lat ${data.lat}, Lon ${data.lon}", fontSize = 12.sp, color = Color.DarkGray)
                        }
                    }
                }

                // Kartu Analisis Prediksi Kualitas & Rekomendasi (ML Engine)
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = Color(0xFF6366F1))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Prediksi Mutu & Rekomendasi Panen", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))

                        // Status Kematangan
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(maturityColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text("Status Kematangan Tebu:", fontSize = 12.sp, color = Color.Gray)
                            Text(maturityStatus, fontWeight = FontWeight.Bold, color = maturityColor, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(maturityDesc, fontSize = 12.sp, color = Color.DarkGray)
                        }

                        // Estimasi Rendemen
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Estimasi Rendemen Gula:", color = Color.Gray, fontSize = 13.sp)
                                Text("Perkiraan hasil nira kristal", color = Color.LightGray, fontSize = 11.sp)
                            }
                            Text(
                                String.format(Locale.US, "%.2f %%", estimatedRendemen),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = greenColor
                            )
                        }

                        // Rekomendasi Petugas Lapangan
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Catatan Lapangan PT Rajawali:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.DarkGray)
                                Text(
                                    if (brixValue >= 18.0)
                                        "Data siap diproses untuk surat perintah tebang angkut (SPTA) ke pabrik gula."
                                    else
                                        "Jadwalkan pemantauan ulang petak sebelum surat perintah tebang diterbitkan.",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data pemindaian tidak ditemukan")
            }
        }
    }
}
