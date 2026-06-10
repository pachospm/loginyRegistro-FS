package com.applogin.loginyregistro.core.network

import android.content.Context
import com.applogin.loginyregistro.BuildConfig
import com.applogin.loginyregistro.authapp.data.local.TokenManager
import com.applogin.loginyregistro.authapp.data.remote.AuthApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Construye Retrofit y OkHttp. Cambia BuildConfig.BASE_URL en app/build.gradle.kts para otra API.
object RetrofitClient {
    fun createAuthApiService(context: Context): AuthApiService {
        val tokenManager = TokenManager(context.applicationContext)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}
