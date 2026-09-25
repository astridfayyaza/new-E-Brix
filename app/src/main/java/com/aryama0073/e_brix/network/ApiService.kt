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
        // 🔹 IP USB (127.0.0.1:3000) & IP Wi-Fi Komputer (10.66.178.175:3000) 🔹
        const val BASE_URL = "http://127.0.0.1:3000/"
        const val PC_WIFI_IP = "10.66.178.175:3000"

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
                    val fallbackUrl = if (originalUrl.contains("127.0.0.1:3000")) {
                        originalUrl.replace("127.0.0.1:3000", PC_WIFI_IP)
                    } else if (originalUrl.contains("10.0.2.2:3000")) {
                        originalUrl.replace("10.0.2.2:3000", PC_WIFI_IP)
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
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
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
