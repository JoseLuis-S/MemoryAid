package com.alberti.memoryaid.ui.login

import androidx.lifecycle.ViewModel
import com.alberti.memoryaid.data.local.SessionManager
import com.alberti.memoryaid.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun entrarComoUsuario() {
        sessionManager.setRole(UserRole.USER)
        _uiState.update { it.copy(loginExitoso = true) }
    }

    fun mostrarDialogoPin() {
        _uiState.update { it.copy(mostrarDialogoPin = true, errorPin = null, pinInput = "") }
    }

    fun ocultarDialogoPin() {
        _uiState.update { it.copy(mostrarDialogoPin = false) }
    }

    fun alCambiarPin(nuevoPin: String) {
        if (nuevoPin.length <= 4) {
            _uiState.update { it.copy(pinInput = nuevoPin, errorPin = null) }
        }
    }

    fun validarPinAdmin() {
        val esValido = sessionManager.loginComoAdmin(_uiState.value.pinInput)
        if (esValido) {
            _uiState.update { it.copy(loginExitoso = true, mostrarDialogoPin = false) }
        } else {
            _uiState.update { it.copy(errorPin = "PIN incorrecto") }
        }
    }
}