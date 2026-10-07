package com.aryama0073.e_brix.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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

    val jenisTebuOptions = remember {
        listOf(
            "Bululawang (BL)",
            "PS 862",
            "PS 881",
            "PSJK 922",
            "Kidang Kencana (KK)",
            "VMC 76-16",
            "M 442-51",
            "Lainnya"
        )
    }

    var peta by remember { mutableStateOf("") }
    var jenisTebu by remember { mutableStateOf(jenisTebuOptions[0]) }
    var expandedJenisTebu by remember { mutableStateOf(false) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var brix by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf("") }

    val greenColor = Color(0xFF059669)
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var isInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(editId) {
        if (!isInitialized && editId != null && editId != 0) {
            val existingData = viewModel.getDataById(editId) ?: dataList.find { it.id == editId }
            if (existingData != null) {
                peta = existingData.petak
                if (existingData.jenisTebu.isNotBlank()) {
                    jenisTebu = existingData.jenisTebu
                }
                imageBitmap = existingData.bitmap
                brix = existingData.brix
                latitude = existingData.lat
                longitude = existingData.lon
                timestamp = existingData.timestamp
                isInitialized = true
            }
        }
    }

    val isFormValid =
        peta.isNotBlank() &&
                jenisTebu.isNotBlank() &&
                brix.isNotBlank() &&
                latitude.isNotBlank() &&
                longitude.isNotBlank()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            if (bitmap != null) {
                imageBitmap = bitmap

                recognizeText(bitmap) {
                    brix = it.replace("\n", "").replace("\r", "")

                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                    timestamp = sdf.format(Date())
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }
    }

    val onCameraClick = {
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

        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val isWideScreen = maxWidth >= 600.dp || isLandscape

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                if (isWideScreen) {
                    // Responsive Wide Layout (Tablet / Landscape): 2 Columns side-by-side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Left Column: Camera Box Image
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(280.dp)
                                .clickable { onCameraClick() }
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

                        // Right Column: Form Fields
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = peta,
                                onValueChange = { peta = it.replace("\n", "").replace("\r", "") },
                                label = { Text("Petak") },
                                singleLine = true,
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                modifier = Modifier.fillMaxWidth()
                            )

                            ExposedDropdownMenuBox(
                                expanded = expandedJenisTebu,
                                onExpandedChange = { expandedJenisTebu = !expandedJenisTebu },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = jenisTebu,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Jenis Tebu") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJenisTebu) },
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                    modifier = Modifier
                                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth()
                                )

                                ExposedDropdownMenu(
                                    expanded = expandedJenisTebu,
                                    onDismissRequest = { expandedJenisTebu = false }
                                ) {
                                    jenisTebuOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                jenisTebu = option
                                                expandedJenisTebu = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = brix,
                                onValueChange = { brix = it.replace("\n", "").replace("\r", "") },
                                label = { Text("Brix") },
                                singleLine = true,
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = latitude,
                                    onValueChange = { latitude = it.replace("\n", "").replace("\r", "") },
                                    label = { Text("Latitude") },
                                    singleLine = true,
                                    maxLines = 1,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = longitude,
                                    onValueChange = { longitude = it.replace("\n", "").replace("\r", "") },
                                    label = { Text("Longitude") },
                                    singleLine = true,
                                    maxLines = 1,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                } else {
                    // Responsive Smartphone Layout (Portrait): Single Column
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = peta,
                            onValueChange = { peta = it.replace("\n", "").replace("\r", "") },
                            label = { Text("Petak") },
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )

                        ExposedDropdownMenuBox(
                            expanded = expandedJenisTebu,
                            onExpandedChange = { expandedJenisTebu = !expandedJenisTebu },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = jenisTebu,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Jenis Tebu") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJenisTebu) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expandedJenisTebu,
                                onDismissRequest = { expandedJenisTebu = false }
                            ) {
                                jenisTebuOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            jenisTebu = option
                                            expandedJenisTebu = false
                                        }
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clickable { onCameraClick() }
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

                        OutlinedTextField(
                            value = brix,
                            onValueChange = { brix = it.replace("\n", "").replace("\r", "") },
                            label = { Text("Brix") },
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = latitude,
                            onValueChange = { latitude = it.replace("\n", "").replace("\r", "") },
                            label = { Text("Latitude") },
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = longitude,
                            onValueChange = { longitude = it.replace("\n", "").replace("\r", "") },
                            label = { Text("Longitude") },
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                                    petak = peta.trim(),
                                    jenisTebu = jenisTebu.trim(),
                                    bitmap = imageBitmap,
                                    brix = brix.trim(),
                                    lat = latitude.trim(),
                                    lon = longitude.trim(),
                                    timestamp = currentTimestamp
                                )
                                viewModel.updateData(updatedData) {
                                    onBack()
                                }
                            } else {
                                val newData = ScanData(
                                    id = 0,
                                    petak = peta.trim(),
                                    jenisTebu = jenisTebu.trim(),
                                    bitmap = imageBitmap,
                                    brix = brix.trim(),
                                    lat = latitude.trim(),
                                    lon = longitude.trim(),
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
}
