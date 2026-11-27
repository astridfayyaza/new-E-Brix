package com.aryama0073.e_brix.ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.aryama0073.e_brix.location.LocationHelper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryama0073.e_brix.data.ScanData
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
    var brix by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf("") }

    val greenColor = Color(0xFF059669)
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var isInitialized by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isFetchingLocation by remember { mutableStateOf(false) }

    val fetchGpsLocation: () -> Unit = {
        isFetchingLocation = true
        LocationHelper.getCurrentLocation(
            context = context,
            onLocationReceived = { lat, lon ->
                latitude = String.format(Locale.US, "%.6f", lat)
                longitude = String.format(Locale.US, "%.6f", lon)
                isFetchingLocation = false
                Toast.makeText(context, "Koordinat GPS berhasil diambil", Toast.LENGTH_SHORT).show()
            },
            onError = { errMsg ->
                isFetchingLocation = false
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show()
            }
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchGpsLocation()
        } else {
            Toast.makeText(context, "Izin lokasi diperlukan untuk geotagging GPS otomatis", Toast.LENGTH_SHORT).show()
        }
    }

    val onGpsButtonClick: () -> Unit = {
        if (LocationHelper.hasLocationPermission(context)) {
            fetchGpsLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(editId) {
        if (!isInitialized && editId != null && editId != 0) {
            val existingData = viewModel.getDataById(editId) ?: dataList.find { it.id == editId }
            if (existingData != null) {
                peta = existingData.petak
                if (existingData.jenisTebu.isNotBlank()) {
                    jenisTebu = existingData.jenisTebu
                }
                brix = existingData.brix
                latitude = existingData.lat
                longitude = existingData.lon
                timestamp = existingData.timestamp
                isInitialized = true
            }
        } else if (!isInitialized && (editId == null || editId == 0)) {
            if (latitude.isBlank() && longitude.isBlank() && LocationHelper.hasLocationPermission(context)) {
                fetchGpsLocation()
            }
        }
    }
            }
            }
        }
    }

    val isFormValid =
        peta.isNotBlank() &&
                jenisTebu.isNotBlank() &&
                brix.isNotBlank() &&
                latitude.isNotBlank() &&
                longitude.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                title = {
                    Text(
                        text = if (editId != null && editId != 0) "Edit Data Scan" else "Tambah Data Scan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                }
            )
        }
    ) { padding ->

        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
        ) {
            val isWideScreen = maxWidth >= 600.dp || isLandscape

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (isWideScreen) {
                    // Responsive Wide Layout (Tablet / Landscape): 2 Columns
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Left Column: Information Lahan & Varietas
                        ElevatedCard(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = null,
                                        tint = greenColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Informasi Lahan & Varietas",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = greenColor
                                    )
                                }

                                OutlinedTextField(
                                    value = peta,
                                    onValueChange = { peta = it.replace("\n", "").replace("\r", "") },
                                    label = { Text("Petak / Blok Lahan") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Map,
                                            contentDescription = null,
                                            tint = Color.Gray
                                        )
                                    },
                                    singleLine = true,
                                    maxLines = 1,
                                    shape = RoundedCornerShape(14.dp),
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
                                        label = { Text("Jenis Tebu (Varietas)") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Eco,
                                                contentDescription = null,
                                                tint = greenColor
                                            )
                                        },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJenisTebu) },
                                        shape = RoundedCornerShape(14.dp),
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
                                                text = {
                                                    Text(
                                                        text = option,
                                                        fontWeight = if (option == jenisTebu) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                },
                                                onClick = {
                                                    jenisTebu = option
                                                    expandedJenisTebu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Right Column: Hasil Pengukuran & GPS
                        ElevatedCard(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = null,
                                        tint = greenColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Hasil Pengukuran & Lokasi",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = greenColor
                                    )
                                }

                                OutlinedTextField(
                                    value = brix,
                                    onValueChange = { brix = it.replace("\n", "").replace("\r", "") },
                                    label = { Text("Nilai Brix (%)") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Speed,
                                            contentDescription = null,
                                            tint = greenColor
                                        )
                                    },
                                    singleLine = true,
                                    maxLines = 1,
                                    shape = RoundedCornerShape(14.dp),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = latitude,
                                        onValueChange = { latitude = it.replace("\n", "").replace("\r", "") },
                                        label = { Text("Latitude") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = Color.Gray
                                            )
                                        },
                                        singleLine = true,
                                        maxLines = 1,
                                        shape = RoundedCornerShape(14.dp),
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
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = Color.Gray
                                            )
                                        },
                                        singleLine = true,
                                        maxLines = 1,
                                        shape = RoundedCornerShape(14.dp),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                OutlinedButton(
                                    onClick = onGpsButtonClick,
                                    enabled = !isFetchingLocation,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = greenColor),
                                    border = BorderStroke(1.dp, greenColor.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (isFetchingLocation) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = greenColor
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Mengambil Koordinat GPS...", fontSize = 13.sp)
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.MyLocation,
                                            contentDescription = "GPS",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (latitude.isBlank()) "Ambil Koordinat GPS Otomatis" else "Perbarui Lokasi GPS Terkini",
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Smartphone Layout (Portrait): Card Sections
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = null,
                                    tint = greenColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Informasi Lahan & Varietas",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = greenColor
                                )
                            }

                            OutlinedTextField(
                                value = peta,
                                onValueChange = { peta = it.replace("\n", "").replace("\r", "") },
                                label = { Text("Petak / Blok Lahan") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                },
                                singleLine = true,
                                maxLines = 1,
                                shape = RoundedCornerShape(14.dp),
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
                                    label = { Text("Jenis Tebu (Varietas)") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Eco,
                                            contentDescription = null,
                                            tint = greenColor
                                        )
                                    },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJenisTebu) },
                                    shape = RoundedCornerShape(14.dp),
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
                                            text = {
                                                Text(
                                                    text = option,
                                                    fontWeight = if (option == jenisTebu) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            onClick = {
                                                jenisTebu = option
                                                expandedJenisTebu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = greenColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Hasil Pengukuran & Lokasi",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = greenColor
                                )
                            }

                            OutlinedTextField(
                                value = brix,
                                onValueChange = { brix = it.replace("\n", "").replace("\r", "") },
                                label = { Text("Nilai Brix (%)") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = null,
                                        tint = greenColor
                                    )
                                },
                                singleLine = true,
                                maxLines = 1,
                                shape = RoundedCornerShape(14.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = latitude,
                                    onValueChange = { latitude = it.replace("\n", "").replace("\r", "") },
                                    label = { Text("Latitude") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Color.Gray
                                        )
                                    },
                                    singleLine = true,
                                    maxLines = 1,
                                    shape = RoundedCornerShape(14.dp),
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
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Color.Gray
                                        )
                                    },
                                    singleLine = true,
                                    maxLines = 1,
                                    shape = RoundedCornerShape(14.dp),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                                OutlinedButton(
                                    onClick = onGpsButtonClick,
                                    enabled = !isFetchingLocation,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = greenColor),
                                    border = BorderStroke(1.dp, greenColor.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (isFetchingLocation) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = greenColor
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Mengambil Koordinat GPS...", fontSize = 13.sp)
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.MyLocation,
                                            contentDescription = "GPS",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (latitude.isBlank()) "Ambil Koordinat GPS Otomatis" else "Perbarui Lokasi GPS Terkini",
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFDC2626)
                        ),
                        border = BorderStroke(1.5.dp, Color(0xFFFCA5A5))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Batal", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
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
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = greenColor,
                            disabledContainerColor = Color.LightGray
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (editId != null && editId != 0) "Simpan Edit" else "Simpan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
