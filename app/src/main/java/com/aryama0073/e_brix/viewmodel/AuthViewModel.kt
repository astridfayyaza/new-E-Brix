package com.aryama0073.e_brix.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aryama0073.e_brix.data.UserData
import com.aryama0073.e_brix.network.ApiService
import com.aryama0073.e_brix.network.LoginRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("ebrix_user_session", Context.MODE_PRIVATE)
    private val apiService = ApiService.create()

    private val _currentUser = MutableStateFlow<UserData?>(null)
    val currentUser: StateFlow<UserData?> = _currentUser

    private val _isCheckingAuth = MutableStateFlow(true)
    val isCheckingAuth: StateFlow<Boolean> = _isCheckingAuth

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError

    init {
        checkExistingUser()
    }

    fun checkExistingUser() {
        val savedEmail = prefs.getString("user_email", null)
        val savedName = prefs.getString("user_name", null)
        val savedPhoto = prefs.getString("user_photo", null)
        val savedRole = prefs.getString("user_role", "petugas_lapangan") ?: "petugas_lapangan"
        val savedToken = prefs.getString("auth_token", null)

        if (!savedEmail.isNullOrEmpty()) {
            _currentUser.value = UserData(
                email = savedEmail,
                displayName = savedName ?: savedEmail.substringBefore("@"),
                photoUrl = savedPhoto,
                role = savedRole,
                token = savedToken
            )
        } else {
            val account = GoogleSignIn.getLastSignedInAccount(getApplication())
            if (account != null && !account.email.isNullOrEmpty()) {
                saveUserSession(
                    email = account.email!!,
                    displayName = account.displayName ?: account.givenName ?: "User",
                    photoUrl = account.photoUrl?.toString(),
                    role = "petugas_lapangan"
                )
            } else {
                _currentUser.value = null
            }
        }
        _isCheckingAuth.value = false
    }

    fun loginWithEmail(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            try {
                val response = apiService.login(LoginRequest(email.trim(), password.trim()))
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    val userData = body.data
                    val displayName = userData?.username ?: email.substringBefore("@")
                    val role = userData?.role ?: "petugas_lapangan"
                    val token = body.accessToken

                    saveUserSession(
                        email = email.trim(),
                        displayName = displayName,
                        photoUrl = null,
                        role = role,
                        token = token
                    )
                    onResult(true, null)
                } else {
                    val errMsg = if (response.code() == 401) {
                        "Email atau password salah"
                    } else {
                        "Gagal masuk (${response.code()}): ${response.message()}"
                    }
                    _authError.value = errMsg
                    onResult(false, errMsg)
                }
            } catch (e: Exception) {
                val errMsg = "Koneksi server gagal: ${e.localizedMessage ?: "Periksa koneksi backend"}"
                _authError.value = errMsg
                onResult(false, errMsg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSignInSuccess(
        email: String,
        displayName: String?,
        photoUrl: String?,
        role: String = "petugas_lapangan",
        token: String? = null
    ) {
        val finalName = displayName ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
        saveUserSession(email, finalName, photoUrl, role, token)
    }

    private fun saveUserSession(
        email: String,
        displayName: String?,
        photoUrl: String?,
        role: String = "petugas_lapangan",
        token: String? = null
    ) {
        prefs.edit()
            .putString("user_email", email)
            .putString("user_name", displayName)
            .putString("user_photo", photoUrl)
            .putString("user_role", role)
            .putString("auth_token", token)
            .apply()

        _currentUser.value = UserData(
            email = email,
            displayName = displayName,
            photoUrl = photoUrl,
            role = role,
            token = token
        )
    }

    fun signOut(onSuccess: () -> Unit) {
        prefs.edit().clear().apply()
        _currentUser.value = null

        try {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val client = GoogleSignIn.getClient(getApplication(), gso)
            client.signOut().addOnCompleteListener {
                onSuccess()
            }
        } catch (_: Exception) {
            onSuccess()
        }
    }
}
