package com.applogin.loginyregistro.authapp.presentation.home

import com.applogin.loginyregistro.authapp.domain.model.User

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val errorMessage: String? = null,
    val isLoggedOut: Boolean = false
)
