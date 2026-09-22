package com.aryama0073.e_brix.ui

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aryama0073.e_brix.viewmodel.ScanViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ScanViewModel,
    onAddClick: () -> Unit,
    onItemClick: (Int) -> Unit
) {
    val dataList by viewModel.dataList.collectAsState()

    val greenColor = Color(0xFF059669)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("E-Brix") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.Unspecified
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = greenColor
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(dataList) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable { onItemClick(item.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Petak: ${item.petak}")
                        Text("Brix: ${item.brix}")
                        Text("Waktu: ${item.timestamp}")
                    }
                }
            }
        }
    }
}