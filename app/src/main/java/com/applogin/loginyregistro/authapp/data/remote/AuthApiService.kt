package com.applogin.loginyregistro.authapp.data.remote

import com.applogin.loginyregistro.authapp.data.remote.dto.AuthResponseDto
import com.applogin.loginyregistro.authapp.data.remote.dto.LoginRequestDto
import com.applogin.loginyregistro.authapp.data.remote.dto.RegisterRequestDto
import com.applogin.loginyregistro.authapp.data.remote.dto.RegisterResponseDto
import com.applogin.loginyregistro.authapp.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Define los endpoints REST que consume la app. La UI nunca llama esta interfaz directamente.
interface AuthApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<RegisterResponseDto>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<AuthResponseDto>

    @GET("users/me")
    suspend fun getProfile(): Response<UserDto>
}
