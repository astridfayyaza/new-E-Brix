package com.aryama0073.e_brix.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("api/scans")
    suspend fun getAllScans(): Response<List<ScanDto>>

    @POST("api/scans")
    suspend fun createScan(@Body scanDto: ScanDto): Response<ScanDto>

    companion object {
        // 🔹 KONFIGURASI IP BACKEND 🔹
        // HP Asli via USB (dengan 'adb reverse tcp:3000 tcp:3000'): "http://127.0.0.1:3000/"
        const val BASE_URL = "http://127.0.0.1:3000/"

        fun create(baseUrl: String = BASE_URL): ApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                // Menambahkan header "Connection: close" mencegah bug 'unexpected end of stream' pada Node.js lokal
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("Connection", "close")
                        .build()
                    chain.proceed(request)
                }
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

            val formattedUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

            return Retrofit.Builder()
                .baseUrl(formattedUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
