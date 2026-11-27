package com.aryama0073.e_brix.network

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("data") val data: AuthUserData? = null
)

data class AuthUserData(
    @SerializedName("id_user") val idUser: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("status") val status: String? = null
)

