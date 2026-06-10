package com.applogin.loginyregistro.authapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.applogin.loginyregistro.core.util.Resource
import com.applogin.loginyregistro.authapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Valida la sesion al abrir la app: token en DataStore y perfil desde /users/me.
class AuthLoadingViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthLoadingUiState>(AuthLoadingUiState.Loading)
    val uiState: StateFlow<AuthLoadingUiState> = _uiState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val token = repository.token.first()
            if (token.isNullOrBlank()) {
                _uiState.value = AuthLoadingUiState.Unauthenticated
                return@launch
            }

            when (repository.getProfile()) {
                is Resource.Success -> _uiState.value = AuthLoadingUiState.Authenticated
                is Resource.Error -> {
                    repository.logout()
                    _uiState.value = AuthLoadingUiState.Unauthenticated
                }
                Resource.Loading -> _uiState.value = AuthLoadingUiState.Loading
            }
        }
    }

    class Factory(private val repository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthLoadingViewModel(repository) as T
        }
    }
}
