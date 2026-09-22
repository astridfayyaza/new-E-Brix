package com.aryama0073.e_brix.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    onBack: () -> Unit
) {
    val data = viewModel.getDataById(id)
    val greenColor = Color(0xFF7FCC52)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Data") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Unspecified
                )
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

                Text("Petak: ${data.petak}")

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

                Text("Brix: ${data.brix}")
                Text("Latitude: ${data.lat}")
                Text("Longitude: ${data.lon}")
                Text("Timestamp: ${data.timestamp}")
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