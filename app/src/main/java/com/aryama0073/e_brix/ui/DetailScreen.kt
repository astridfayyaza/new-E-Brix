package com.aryama0073.e_brix.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.aryama0073.e_brix.viewmodel.ScanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: ScanViewModel,
    id: Int,
    onEditClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val data = viewModel.getDataById(id)
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
                        viewModel.deleteData(data.id) {
                            showDeleteDialog = false
                            onBack()
                        }
                    }
                ) {
                    Text("Hapus", color = Color.Red)
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
                title = { Text("Detail Data Scan") },
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
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                Text("Petak: ${data.petak}", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(12.dp))

                data.bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Brix: ${data.brix}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Latitude: ${data.lat}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Longitude: ${data.lon}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Timestamp: ${data.timestamp}", color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text("Data tidak ditemukan")
            }
        }
    }
}
