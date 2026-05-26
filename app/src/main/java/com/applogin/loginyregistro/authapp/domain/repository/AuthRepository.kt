package com.applogin.loginyregistro.authapp.domain.repository

import okhttp3.Response
import com.applogin.loginyregistro.authapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    //Flujo que emite el token guardado o null si no existe la sesión
    val token: Flow<String>
    // Registrar un usuario usando nambe,email y password
    //Devolver Resource<String> porque el backedn retorna un mensaje
    suspend fun register(name: String, email: String, password:String): Resource<String>
    suspend fun login(email: String, password: String): Resource<User>

    //Consulta el perfil de usuairo autenticado usando el token guardado
    suspend fun getProfile(): Resource<User>
    // Cierra sesión
    suspend fun logout()
}