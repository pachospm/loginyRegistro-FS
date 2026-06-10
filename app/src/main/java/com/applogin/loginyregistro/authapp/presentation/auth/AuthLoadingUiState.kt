package com.applogin.loginyregistro.authapp.presentation.auth

sealed interface AuthLoadingUiState {
    data object Loading : AuthLoadingUiState
    data object Authenticated : AuthLoadingUiState
    data object Unauthenticated : AuthLoadingUiState
}
