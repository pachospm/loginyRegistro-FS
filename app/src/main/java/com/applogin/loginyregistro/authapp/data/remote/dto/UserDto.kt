package com.applogin.loginyregistro.authapp.data.remote.dto

import com.applogin.loginyregistro.authapp.domain.model.User

data class UserDto(
    val id: Int?,
    val name: String?,
    val email: String?
)

fun UserDto.toDomain(): User = User(
    id = id ?: 0,
    name = name.orEmpty(),
    email = email.orEmpty()
)
