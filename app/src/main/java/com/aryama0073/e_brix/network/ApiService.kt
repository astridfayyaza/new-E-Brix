package com.aryama0073.e_brix.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.io.IOException
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("api/scans")
    suspend fun getAllScans(): Response<List<ScanDto>>

    @POST("api/scans")
    suspend fun createScan(@Body scanDto: ScanDto): Response<ScanDto>

    @PUT("api/scans/{id}")
    suspend fun updateScan(@Path("id") id: Int, @Body scanDto: ScanDto): Response<ScanDto>

    @DELETE("api/scans/{id}")
    suspend fun deleteScan(@Path("id") id: Int): Response<Unit>

    companion object {
        // 🔹 IP Server Backend Flask (Port 5000) 🔹
        // Primary: IP Wi-Fi PC untuk HP Fisik (10.20.112.225:5000)
        // Fallback: 10.0.2.2:5000 (untuk Android Emulator) & 127.0.0.1:5000 (untuk USB Reverse)
        const val BASE_URL = "http://10.20.112.225:5000/"
        const val EMULATOR_IP = "10.0.2.2:5000"

        fun create(baseUrl: String = BASE_URL): ApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val failoverInterceptor = Interceptor { chain ->
                val request = chain.request()
                try {
                    chain.proceed(
                        request.newBuilder()
                            .header("Connection", "close")
                            .build()
                    )
                } catch (_: IOException) {
                    val originalUrl = request.url.toString()
                    val fallbackUrl = if (originalUrl.contains("10.20.112.225:5000")) {
                        originalUrl.replace("10.20.112.225:5000", EMULATOR_IP)
                    } else if (originalUrl.contains("10.0.2.2:5000")) {
                        originalUrl.replace("10.0.2.2:5000", "127.0.0.1:5000")
                    } else {
                        originalUrl
                    }

                    val fallbackRequest = request.newBuilder()
                        .url(fallbackUrl)
                        .header("Connection", "close")
                        .build()

                    chain.proceed(fallbackRequest)
                }
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(failoverInterceptor)
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
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
