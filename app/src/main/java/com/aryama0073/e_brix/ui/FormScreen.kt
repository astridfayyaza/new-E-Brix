package com.aryama0073.e_brix.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.aryama0073.e_brix.data.ScanData
import com.aryama0073.e_brix.ocr.recognizeText
import com.aryama0073.e_brix.viewmodel.ScanViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: ScanViewModel,
    editId: Int? = null,
    onBack: () -> Unit
) {

    val dataList by viewModel.dataList.collectAsState()

    var peta by remember { mutableStateOf("") }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var brix by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf("") }

    val greenColor = Color(0xFF059669)
    val context = LocalContext.current

    // Prefill data jika dalam mode EDIT secara reaktif
    LaunchedEffect(editId, dataList) {
        if (editId != null && editId != 0) {
            val existingData = dataList.find { it.id == editId } ?: viewModel.getDataById(editId)
            if (existingData != null) {
                peta = existingData.petak
                imageBitmap = existingData.bitmap
                brix = existingData.brix
                latitude = existingData.lat
                longitude = existingData.lon
                timestamp = existingData.timestamp
            }
        }
    }

    // VALIDASI FORM
    val isFormValid =
        peta.isNotBlank() &&
                brix.isNotBlank() &&
                latitude.isNotBlank() &&
                longitude.isNotBlank()

    // CAMERA RESULT
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val bitmap = result.data?.extras?.get("data") as? Bitmap

            if (bitmap != null) {
                imageBitmap = bitmap

                recognizeText(bitmap) {
                    brix = it

                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                    timestamp = sdf.format(Date())
                }
            }
        }
    }

    // PERMISSION
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor,
                    titleContentColor = Color.White
                ),
                title = { Text(if (editId != null && editId != 0) "Edit Data Scan" else "Tambah Data") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // 🔹 PETAK
            OutlinedTextField(
                value = peta,
                onValueChange = { peta = it },
                label = { Text("Petak") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 FOTO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED -> {

                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                cameraLauncher.launch(intent)
                            }

                            else -> {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tambah / Ambil Gambar", color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 BRIX
            OutlinedTextField(
                value = brix,
                onValueChange = { brix = it },
                label = { Text("Brix") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 LATITUDE
            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Latitude") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 LONGITUDE
            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Longitude") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // 🔹 BUTTON
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedButton(
                    onClick = onBack,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Batal")
                }

                Button(
                    onClick = {
                        val currentTimestamp = if (timestamp.isNotBlank()) timestamp else {
                            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                            sdf.format(Date())
                        }

                        if (editId != null && editId != 0) {
                            val updatedData = ScanData(
                                id = editId,
                                petak = peta,
                                bitmap = imageBitmap,
                                brix = brix,
                                lat = latitude,
                                lon = longitude,
                                timestamp = currentTimestamp
                            )
                            viewModel.updateData(updatedData) {
                                onBack()
                            }
                        } else {
                            val newData = ScanData(
                                id = 0,
                                petak = peta,
                                bitmap = imageBitmap,
                                brix = brix,
                                lat = latitude,
                                lon = longitude,
                                timestamp = currentTimestamp
                            )
                            viewModel.addData(newData) {
                                onBack()
                            }
                        }
                    },
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = greenColor,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text(if (editId != null && editId != 0) "Simpan Perubahan" else "Simpan")
                }
            }
        }
    }
}
