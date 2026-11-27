package com.aryama0073.e_brix.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aryama0073.e_brix.data.ScanData
import com.aryama0073.e_brix.viewmodel.AuthViewModel
import com.aryama0073.e_brix.viewmodel.ScanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ScanViewModel,
    authViewModel: AuthViewModel,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onItemClick: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val dataList by viewModel.dataList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    var itemToDelete by remember { mutableStateOf<ScanData?>(null) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val greenColor = Color(0xFF059669)

    if (showProfileDialog && currentUser != null) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showProfileDialog = false
                        authViewModel.signOut {
                            onLogout()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Keluar (Logout)")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showProfileDialog = false }) {
                    Text("Tutup")
                }
            },
            title = {
                Text(
                    text = "Informasi Pengguna",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!currentUser?.photoUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = currentUser?.photoUrl,
                            contentDescription = "Foto Profil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(greenColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = greenColor,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = currentUser?.displayName ?: "Pengguna E-Brix",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentUser?.email ?: "",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Role Badge
                    Surface(
                        color = if (currentUser?.isPetugas == true) Color(0xFFE0E7FF) else Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = if (currentUser?.isPetugas == true) Color(0xFF4338CA) else Color(0xFF15803D),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentUser?.roleLabel ?: "Petani Mitra",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (currentUser?.isPetugas == true) Color(0xFF4338CA) else Color(0xFF15803D)
                            )
                        }
                    }
                }
            }
        )
    }

    if (itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Hapus Data") },
            text = { Text("Apakah Anda yakin ingin menghapus data petak '${itemToDelete?.petak}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        itemToDelete?.let { scan ->
                            viewModel.deleteData(scan.id)
                        }
                        itemToDelete = null
                    }
                ) {
                    Text("Hapus", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("E-Brix Mobile", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        currentUser?.let { user ->
                            Text(
                                text = "${user.displayName ?: user.email} • ${if (user.isPetugas) "Petugas" else "Petani"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.85f),
                                maxLines = 1
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { viewModel.fetchScansFromDatabase() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showProfileDialog = true }) {
                        if (!currentUser?.photoUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = currentUser?.photoUrl,
                                contentDescription = "Profil User",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profil User",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = greenColor
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
        ) {
            if (isLoading && dataList.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = greenColor
                )
            } else if (errorMessage != null && dataList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = errorMessage ?: "Terjadi kesalahan",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.fetchScansFromDatabase() },
                        colors = ButtonDefaults.buttonColors(containerColor = greenColor)
                    ) {
                        Text("Coba Lagi")
                    }
                }
            } else if (dataList.isEmpty()) {
                Text(
                    text = "Belum ada data scan di database.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(dataList) { item ->
                        val brixVal = item.brix.toDoubleOrNull() ?: 0.0
                        val (badgeText, badgeBg, badgeTextColor) = when {
                            brixVal < 16.0 -> Triple("Vegetatif", Color(0xFFFEF3C7), Color(0xFFB45309))
                            brixVal in 16.0..18.0 -> Triple("Awal Masak", Color(0xFFDBEAFE), Color(0xFF1D4ED8))
                            brixVal in 18.01..21.5 -> Triple("Siap Panen", Color(0xFFD1FAE5), Color(0xFF047857))
                            else -> Triple("Lewat Masak", Color(0xFFFEE2E2), Color(0xFFB91C1C))
                        }

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemClick(item.id) },
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.petak,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            color = badgeBg,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = badgeText,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = badgeTextColor,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.jenisTebu,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = greenColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Kadar Brix: ${item.brix}%  •  Est. Rendemen: ${String.format(java.util.Locale.US, "%.1f", brixVal * 0.70)}%",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Waktu: ${item.timestamp.ifBlank { "-" }}",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Row {
                                    IconButton(onClick = { onEditClick(item.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Data",
                                            tint = greenColor
                                        )
                                    }
                                    IconButton(onClick = { itemToDelete = item }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Hapus Data",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
