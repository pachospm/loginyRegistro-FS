package com.applogin.loginyregistro.authapp.data.remote.dto

data class AuthResponseDto(
    val message: String?,
    val token: String?,
    val user: UserDto?
)
