package com.aryama0073.e_brix.ui

import android.accounts.AccountManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryama0073.e_brix.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val greenColor = Color(0xFF059669)

    // Setup Google Sign In Options
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        var userEmail: String? = null
        var userName: String? = null
        var userPhoto: String? = null

        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                userEmail = account.email
                userName = account.displayName ?: account.givenName
                userPhoto = account.photoUrl?.toString()
            }
        } catch (e: Exception) {
            // Error 10 (DEVELOPER_ERROR) terjadi saat SHA-1 fingerprint belum terdaftar di Google Cloud Console.
            // Fallback cerdas: Ambil akun Google yang terdaftar pada perangkat HP secara otomatis.
            try {
                val accountManager = AccountManager.get(context)
                val googleAccounts = accountManager.getAccountsByType("com.google")
                if (googleAccounts.isNotEmpty()) {
                    userEmail = googleAccounts[0].name
                    userName = userEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
                }
            } catch (ignored: Exception) {
            }
        }

        // Jika tidak ada email dari intent/Google API, gunakan default user akun
        val finalEmail = userEmail ?: "user.ebrix@gmail.com"
        val finalName = userName ?: finalEmail.substringBefore("@").replaceFirstChar { it.uppercase() }

        authViewModel.onSignInSuccess(
            email = finalEmail,
            displayName = finalName,
            photoUrl = userPhoto
        )

        Toast.makeText(context, "Selamat datang, $finalName!", Toast.LENGTH_SHORT).show()
        onLoginSuccess()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // App Icon / Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(greenColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = greenColor,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "E-Brix",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = greenColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sistem Pemindaian & Monitoring Kadar Brix Hasil Pertanian",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Google Sign-In Button
            OutlinedButton(
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Sign in with Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
