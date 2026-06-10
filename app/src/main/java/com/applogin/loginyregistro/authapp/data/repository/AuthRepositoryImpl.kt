package com.applogin.loginyregistro.authapp.data.repository

import com.applogin.loginyregistro.core.util.Resource
import com.applogin.loginyregistro.authapp.data.local.TokenManager
import com.applogin.loginyregistro.authapp.data.remote.AuthApiService
import com.applogin.loginyregistro.authapp.data.remote.dto.LoginRequestDto
import com.applogin.loginyregistro.authapp.data.remote.dto.RegisterRequestDto
import com.applogin.loginyregistro.authapp.data.remote.dto.toDomain
import com.applogin.loginyregistro.authapp.domain.model.User
import com.applogin.loginyregistro.authapp.domain.repository.AuthRepository
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override val token: Flow<String?> = tokenManager.token

    override suspend fun register(name: String, email: String, password: String): Resource<String> {
        return try {
            val response = apiService.register(RegisterRequestDto(name, email, password))
            if (response.isSuccessful) {
                Resource.Success(response.body()?.message ?: "Usuario registrado correctamente.")
            } else {
                Resource.Error(response.toFriendlyMessage())
            }
        } catch (exception: IOException) {
            Resource.Error("No se pudo conectar con el servidor.")
        } catch (exception: HttpException) {
            Resource.Error("Ocurrio un error en la comunicacion con el servidor.")
        } catch (exception: Exception) {
            Resource.Error("Ocurrio un error inesperado.")
        }
    }

    override suspend fun login(email: String, password: String): Resource<User> {
        return try {
            val response = apiService.login(LoginRequestDto(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                val token = body?.token
                val user = body?.user

                if (token.isNullOrBlank() || user == null) {
                    Resource.Error("La respuesta del servidor no es valida.")
                } else {
                    tokenManager.saveToken(token)
                    Resource.Success(user.toDomain())
                }
            } else {
                Resource.Error(response.toFriendlyMessage())
            }
        } catch (exception: IOException) {
            Resource.Error("No se pudo conectar con el servidor.")
        } catch (exception: HttpException) {
            Resource.Error("Ocurrio un error en la comunicacion con el servidor.")
        } catch (exception: Exception) {
            Resource.Error("Ocurrio un error inesperado.")
        }
    }

    override suspend fun getProfile(): Resource<User> {
        return try {
            val response = apiService.getProfile()
            if (response.isSuccessful) {
                val user = response.body()
                if (user == null) {
                    Resource.Error("La respuesta del servidor no es valida.")
                } else {
                    Resource.Success(user.toDomain())
                }
            } else {
                Resource.Error(response.toFriendlyMessage())
            }
        } catch (exception: IOException) {
            Resource.Error("No se pudo conectar con el servidor.")
        } catch (exception: HttpException) {
            Resource.Error("Ocurrio un error en la comunicacion con el servidor.")
        } catch (exception: Exception) {
            Resource.Error("Ocurrio un error inesperado.")
        }
    }

    override suspend fun logout() {
        tokenManager.clearToken()
    }

    private fun retrofit2.Response<*>.toFriendlyMessage(): String {
        return when (code()) {
            400 -> "Solicitud invalida. Revisa los datos ingresados."
            401 -> "Credenciales incorrectas o sesion expirada."
            500 -> "El servidor tuvo un problema. Intenta mas tarde."
            else -> "Ocurrio un error inesperado."
        }
    }
}
