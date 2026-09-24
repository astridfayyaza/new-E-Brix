package com.aryama0073.e_brix.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.aryama0073.e_brix.data.UserData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("ebrix_user_session", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<UserData?>(null)
    val currentUser: StateFlow<UserData?> = _currentUser

    private val _isCheckingAuth = MutableStateFlow(true)
    val isCheckingAuth: StateFlow<Boolean> = _isCheckingAuth

    init {
        checkExistingUser()
    }

    fun checkExistingUser() {
        val savedEmail = prefs.getString("user_email", null)
        val savedName = prefs.getString("user_name", null)
        val savedPhoto = prefs.getString("user_photo", null)

        if (!savedEmail.isNullOrEmpty()) {
            _currentUser.value = UserData(
                email = savedEmail,
                displayName = savedName ?: savedEmail.substringBefore("@"),
                photoUrl = savedPhoto
            )
        } else {
            val account = GoogleSignIn.getLastSignedInAccount(getApplication())
            if (account != null && !account.email.isNullOrEmpty()) {
                saveUserSession(
                    email = account.email!!,
                    displayName = account.displayName ?: account.givenName ?: "User",
                    photoUrl = account.photoUrl?.toString()
                )
            } else {
                _currentUser.value = null
            }
        }
        _isCheckingAuth.value = false
    }

    fun onSignInSuccess(email: String, displayName: String?, photoUrl: String?) {
        val finalName = displayName ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
        saveUserSession(email, finalName, photoUrl)
    }

    private fun saveUserSession(email: String, displayName: String?, photoUrl: String?) {
        prefs.edit()
            .putString("user_email", email)
            .putString("user_name", displayName)
            .putString("user_photo", photoUrl)
            .apply()

        _currentUser.value = UserData(
            email = email,
            displayName = displayName,
            photoUrl = photoUrl
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
        } catch (e: Exception) {
            onSuccess()
        }
    }
}
