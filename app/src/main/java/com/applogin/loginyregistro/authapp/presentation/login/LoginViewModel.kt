package com.applogin.loginyregistro.authapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.applogin.loginyregistro.core.util.Resource
import com.applogin.loginyregistro.authapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Recibe eventos de LoginScreen, valida campos y delega el consumo de API al Repository.
class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun login() {
        val currentState = _uiState.value
        val validationError = validate(currentState.email, currentState.password)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.login(currentState.email.trim(), currentState.password)) {
                is Resource.Success -> _uiState.update {
                    it.copy(isLoading = false, isLoginSuccessful = true)
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
                Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
            }
        }
    }

    fun resetNavigationState() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }

    private fun validate(email: String, password: String): String? {
        return when {
            email.isBlank() -> "El correo es obligatorio."
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Ingrese un correo valido."
            password.isBlank() -> "La contrasena es obligatoria."
            password.length < 6 -> "La contrasena debe tener al menos 6 caracteres."
            else -> null
        }
    }

    class Factory(private val repository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(repository) as T
        }
    }
}
