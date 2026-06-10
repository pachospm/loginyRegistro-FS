package com.applogin.loginyregistro.authapp.presentation.register

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

// Maneja el formulario de registro y mantiene la UI independiente de Retrofit.
class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun register() {
        val currentState = _uiState.value
        val validationError = validate(currentState.name, currentState.email, currentState.password)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            when (
                val result = repository.register(
                    currentState.name.trim(),
                    currentState.email.trim(),
                    currentState.password
                )
            ) {
                is Resource.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = result.data,
                        isRegisterSuccessful = true
                    )
                }
                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
                Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
            }
        }
    }

    fun resetNavigationState() {
        _uiState.update { it.copy(isRegisterSuccessful = false) }
    }

    private fun validate(name: String, email: String, password: String): String? {
        return when {
            name.isBlank() -> "El nombre es obligatorio."
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
            return RegisterViewModel(repository) as T
        }
    }
}
