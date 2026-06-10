package com.applogin.loginyregistro.authapp.domain.repository

import com.applogin.loginyregistro.core.util.Resource
import com.applogin.loginyregistro.authapp.domain.model.User
import kotlinx.coroutines.flow.Flow

// Contrato que usan los ViewModels. Oculta Retrofit y DataStore de la capa de presentacion.
interface AuthRepository {
    val token: Flow<String?>

    suspend fun register(name: String, email: String, password: String): Resource<String>
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun getProfile(): Resource<User>
    suspend fun logout()
}
